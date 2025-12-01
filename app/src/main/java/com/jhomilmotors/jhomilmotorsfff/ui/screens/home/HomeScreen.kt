package com.jhomilmotors.jhomilmotorsfff.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductOnSaleDTO
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.components.JhomilLoader
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SessionViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.CategoryViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductOnSaleViewModel
import kotlinx.coroutines.delay

// ==========================================
// UTILIDADES (Animaciones)
// ==========================================

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick(scaleDown: Float = 0.95f, onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) scaleDown else 1f, label = "bounce")

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

// ==========================================
// DATA CLASSES
// ==========================================

data class contentHeaderCarrusel(
    val id: Int,
    val title: String,
    val description: String,
    val imageResId: Int,
    val buttonText: String,
)

data class Product(
    val id: Int,
    val imageUrl: Int,
    val title: String,
    val price: Double,
    val originalPrice: Double? = null,
    val soldCount: String,
)

data class CategoryItem(
    val id: Int,
    val nombre: String,
    val iconUri: String?,
    val onClick: () -> Unit,
)

// ==========================================
// COMPONENTES UI
// ==========================================

@Composable
fun ThemeAwareSearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(2.dp, RoundedCornerShape(25.dp)),
        shape = RoundedCornerShape(25.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "¿Qué estás buscando hoy?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(R.drawable.filter_icon),
                contentDescription = "Filtro",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BannerCardItem(
    data: contentHeaderCarrusel,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    // 1. CONFIGURACIÓN DEL CÍRCULO DE FONDO ("La Bola")
    val circleColor = if (isDark) {
        // Modo Oscuro: Color Primario (Resplandor/Luz)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        // Modo Claro: Color Negro (Sombra/Profundidad)
        // Esto hará que el círculo sea oscuro sobre la tarjeta clara
        Color.Black.copy(alpha = 0.10f)
    }

    // 2. CONFIGURACIÓN DEL BRILLO (SHINE)
    // Lo mantenemos sutil en ambos casos para no "lavar" el diseño
    val shineBrush = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            Color.White.copy(alpha = 0.1f), // 10% de opacidad, muy fino
            Color.Transparent
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .bounceClick(scaleDown = 0.98f, onClick = onButtonClick),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // CAPA 1: CÍRCULO DECORATIVO (FONDO)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 40.dp)
                    .clip(CircleShape)
                    .background(circleColor) // Usamos el color calculado arriba
            )

            // CAPA 2: CONTENIDO (Texto e Imagen)
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // TEXTO
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onButtonClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            contentColor = if (isDark) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = data.buttonText,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // IMAGEN
                Box(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        painter = painterResource(id = data.imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = 10.dp, y = 10.dp)
                            .clip(RoundedCornerShape(bottomEnd = 12.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // CAPA 3: BRILLO SUTIL (OVERLAY)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = shineBrush)
            )
        }
    }
}

@Composable
fun CategoryCircleItem(item: CategoryItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(74.dp)
            .bounceClick(onClick = item.onClick)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.iconUri,
                contentDescription = item.nombre,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.nombre,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ==========================================
// TARJETA DE PRODUCTO API (CON NAVEGACIÓN)
// ==========================================
@Composable
fun ProductCard(
    product: ProductOnSaleDTO,
    modifier: Modifier = Modifier,
    onAddToCart: (Long) -> Unit,
    onViewDetail: (Long) -> Unit
) {
    // Si tiene variantId, es compra directa (+), sino es ver detalle (->)
    val isDirectBuy = product.variantId != null

    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
            // Si tocas la tarjeta entera, vas al detalle SIEMPRE
            .bounceClick { onViewDetail(product.productId) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.logo_jhomil),
                    error = painterResource(R.drawable.logo_jhomil)
                )

                // Badges
                var badgeText: String? = null
                val type = product.discountType ?: ""

                if (type == "x_por_y") {
                    badgeText = "2x1"
                } else if (product.originalPrice > product.finalPrice && product.originalPrice > 0) {
                    val discount = ((product.originalPrice - product.finalPrice) / product.originalPrice * 100).toInt()
                    if (discount > 0) badgeText = "-$discount%"
                }

                if (badgeText != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = badgeText,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // === BOTÓN DE ACCIÓN ===
                FilledIconButton(
                    onClick = {
                        if (isDirectBuy) {
                            onAddToCart(product.variantId!!)
                        } else {
                            onViewDetail(product.productId)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        // Primario si es comprar, Terciario si es ver detalle
                        containerColor = if (isDirectBuy) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        // Icono (+) si compra directa, (->) si es detalle
                        imageVector = if (isDirectBuy) Icons.Default.Add else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Acción",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    if (product.finalPrice == 0.0) {
                        Text(
                            text = "GRATIS",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "S/${String.format("%.2f", product.finalPrice)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    if (product.originalPrice > product.finalPrice) {
                        Text(
                            text = "S/${product.originalPrice.toInt()}",
                            style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// === TARJETA LOCAL (GRID) ===
@Composable
fun ProductCardLocal(
    product: Product,
    modifier: Modifier = Modifier,
    onViewDetail: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
            .bounceClick { onViewDetail(product.id.toLong()) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
            ) {
                Image(
                    painter = painterResource(id = product.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                FilledIconButton(
                    onClick = { onViewDetail(product.id.toLong()) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S/${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = product.soldCount,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SkeletonCard() {
    Card(
        modifier = Modifier.width(160.dp).padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.height(145.dp).fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant))
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.height(15.dp).fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant))
            Spacer(modifier = Modifier.height(5.dp))
            Box(modifier = Modifier.height(15.dp).width(80.dp).background(MaterialTheme.colorScheme.outlineVariant))
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String = "Ver todas") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = action,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { }
        )
    }
}

// ==========================================
// PANTALLA PRINCIPAL
// ==========================================

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Principal(
    navController: NavController,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    productOnSaleViewModel: ProductOnSaleViewModel = hiltViewModel()
) {
    val sessionValid by sessionViewModel.sessionValid.collectAsState()
    val isChecking by sessionViewModel.isChecking.collectAsState()
    val category by categoryViewModel.categories.collectAsState()
    val offerState by productOnSaleViewModel.productsOfferState.collectAsState()

    LaunchedEffect(Unit) { sessionViewModel.checkSession() }
    LaunchedEffect(sessionValid, isChecking) {
        if (!isChecking && !sessionValid) {
            navController.navigate(AppScreens.Login.route) { popUpTo(0) { inclusive = true } }
        }
    }
    LaunchedEffect(Unit) { categoryViewModel.loadCategories() }

    val headerItems = listOf(
        contentHeaderCarrusel(1, "Nueva Colección", "Equípate para la ruta", R.drawable.biker_header, "Ver ahora"),
        contentHeaderCarrusel(2, "Ofertas Flash", "Hasta 50% DSCTO", R.drawable.biker_header, "Comprar"),
        contentHeaderCarrusel(3, "Mantenimiento", "Todo para tu taller", R.drawable.biker_header, "Ver más")
    )
    // Productos dummy (Grid)
    val mostPurchasedProducts = listOf(
        Product(1, R.drawable.motul_oil, "Aceite Motul 7100 10W40 Sintético", 26.00, soldCount = "1.2k vendidos"),
        Product(2, R.drawable.motul_oil, "Lubricante de Cadena C2 Road", 67.00, soldCount = "800 vendidos"),
        Product(3, R.drawable.motul_oil, "Limpiador de frenos Motul", 90.00, soldCount = "500 vendidos"),
        Product(4, R.drawable.motul_oil, "Casco LS2 Stream Evo", 250.00, soldCount = "150 vendidos")
    )

    if (isChecking || !sessionValid) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                JhomilLoader(modifier = Modifier.size(220.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Calentando motores...", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
        }
    } else {
        Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Header
                item {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo_jhomil),
                                contentDescription = "Logo",
                                modifier = Modifier.height(30.dp),
                                contentScale = ContentScale.Fit
                            )
                            IconButton(onClick = { }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.notification_icon),
                                    contentDescription = "Alertas",
                                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // Sticky Search
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        ThemeAwareSearchBar()
                    }
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                // Banners
                item {
                    val pagerState = rememberPagerState(pageCount = { headerItems.size })
                    LaunchedEffect(pagerState) {
                        while (true) {
                            delay(3000)
                            if (!pagerState.isScrollInProgress) {
                                val nextPage = (pagerState.currentPage + 1) % headerItems.size
                                pagerState.animateScrollToPage(nextPage)
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
                            BannerCardItem(
                                data = headerItems[page],
                                onButtonClick = {},
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                Box(modifier = Modifier.padding(3.dp).clip(CircleShape).background(color).size(if (pagerState.currentPage == iteration) 8.dp else 6.dp))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Categorías
                item { SectionHeader(title = "Categorías") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    when (val state = category) {
                        is UiState.Success -> {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(state.data) { cat ->
                                    CategoryCircleItem(
                                        item = CategoryItem(
                                            id = cat.id.toInt(),
                                            nombre = cat.nombre,
                                            iconUri = cat.urlImagenCompleta,
                                            onClick = {
                                                // Navegación a la lista filtrada por categoría
                                                navController.navigate(AppScreens.ProductList.createRoute(cat.id.toInt()))
                                            }
                                        )
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // Ofertas Especiales (API)
                item { SectionHeader(title = "Ofertas Especiales 🔥", action = "") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    when (val state = offerState) {
                        is UiState.Loading -> {
                            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(3) { SkeletonCard() }
                            }
                        }
                        is UiState.Error -> {
                            Text("No se pudieron cargar las ofertas.", modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.error)
                        }
                        is UiState.Success -> {
                            if (state.data.content.isNotEmpty()) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(state.data.content) { product ->
                                        // IMPLEMENTACIÓN DE LA NAVEGACIÓN REAL
                                        ProductCard(
                                            product = product,
                                            modifier = Modifier.width(160.dp),
                                            onAddToCart = { variantId ->
                                                // Acción: Añadir al carrito (Impresión por ahora)
                                                println("ADD TO CART: Variant $variantId")
                                            },
                                            onViewDetail = { productId ->
                                                // Acción: Navegar al detalle del producto
                                                navController.navigate(AppScreens.ProductDetail.createRoute(productId))
                                            }
                                        )
                                    }
                                }
                            } else {
                                Text("No hay ofertas por el momento.", modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                            }
                        }
                        else -> {}
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // Más Vendidos (Grid Vertical)
                item { SectionHeader(title = "Más Vendidos", action = "") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        val rows = mostPurchasedProducts.chunked(2)
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowItems.forEach { product ->
                                    ProductCardLocal(
                                        product = product,
                                        modifier = Modifier.weight(1f),
                                        onViewDetail = { productId ->
                                            // Navegación en productos dummy también (usando ID fake)
                                            navController.navigate(AppScreens.ProductDetail.createRoute(productId))
                                        }
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}