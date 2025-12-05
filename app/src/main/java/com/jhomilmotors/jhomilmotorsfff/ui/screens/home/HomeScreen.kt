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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
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
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductCatalogDTO
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductOnSaleDTO
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.components.JhomilLoader
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SessionViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.CategoryViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.FeedViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductOnSaleViewModel
import kotlinx.coroutines.delay

// ==========================================
// UTILIDADES
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
// DATA CLASSES LOCALES
// ==========================================

data class contentHeaderCarrusel(
    val id: Int,
    val title: String,
    val description: String,
    val imageResId: Int,
    val buttonText: String,
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
    val circleColor = if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.10f)
    val shineBrush = Brush.linearGradient(
        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.1f), Color.Transparent),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .bounceClick(scaleDown = 0.98f, onClick = onButtonClick),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = 40.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onButtonClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            contentColor = if (isDark) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = data.buttonText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.9f)
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
            Box(modifier = Modifier.fillMaxSize().background(brush = shineBrush))
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

@Composable
fun ProductCard(
    product: ProductOnSaleDTO,
    modifier: Modifier = Modifier,
    onAddToCart: (Long) -> Unit,
    onViewDetail: (Long) -> Unit
) {
    val isDirectBuy = product.variantId != null
    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
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
                FilledIconButton(
                    onClick = {
                        if (isDirectBuy) onAddToCart(product.variantId!!) else onViewDetail(product.productId)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isDirectBuy) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
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
                        Text(text = "GRATIS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = "S/${String.format("%.2f", product.finalPrice)}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    if (product.originalPrice > product.finalPrice) {
                        Text(text = "S/${product.originalPrice.toInt()}", style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

// === TARJETA LOCAL (FEED REAL - CATALOG DTO) ===
@Composable
fun ProductCardLocal(
    product: ProductCatalogDTO,
    modifier: Modifier = Modifier,
    onViewDetail: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .padding(bottom = 4.dp)
            .bounceClick { onViewDetail(product.id) },
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
                AsyncImage(
                    model = product.imagenUrl,
                    contentDescription = product.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.logo_jhomil),
                    error = painterResource(R.drawable.logo_jhomil)
                )
                FilledIconButton(
                    onClick = { onViewDetail(product.id) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ver", modifier = Modifier.size(16.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S/${String.format("%.2f", product.precioBase)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = product.marcaNombre ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(bottom = 8.dp),
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
        if (action.isNotEmpty()) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
        }
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
    productOnSaleViewModel: ProductOnSaleViewModel = hiltViewModel(),
    feedViewModel: FeedViewModel = hiltViewModel()
) {
    val sessionValid by sessionViewModel.sessionValid.collectAsState()
    val isChecking by sessionViewModel.isChecking.collectAsState()
    val category by categoryViewModel.categories.collectAsState()
    val offerState by productOnSaleViewModel.productsOfferState.collectAsState()
    val feedState by feedViewModel.feedState.collectAsState()
    val isLoadingMore by feedViewModel.isLoadingMore.collectAsState()

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
                // HEADER
                item {
                    var showMenu by remember { mutableStateOf(false) }
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
                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Opciones",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Ver ubicación de tienda") },
                                        leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                                        onClick = {
                                            showMenu = false
                                            navController.navigate(AppScreens.StoreMap.route)
                                        }
                                    )
                                }
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
                        Box(modifier = Modifier.clickable { navController.navigate(AppScreens.SearchScreen.route) }) {
                            ThemeAwareSearchBar()
                        }
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
                                                navController.navigate(AppScreens.ProductList.createRoute(cat.id.toInt(), cat.nombre))
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
                                items(3) { SkeletonCard(modifier = Modifier.width(160.dp)) }
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
                                        ProductCard(
                                            product = product,
                                            modifier = Modifier.width(160.dp),
                                            onAddToCart = { variantId ->
                                                println("Añadir al carrito Variante: $variantId")
                                            },
                                            onViewDetail = { productId ->
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

                // ==========================================
                // NUESTROS PRODUCTOS (FEED REAL + INFINITE SCROLL)
                // ==========================================
                item { SectionHeader(title = "Nuestros Productos", action = "") }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    when (val state = feedState) {
                        is UiState.Loading -> {
                            // Skeletons de carga inicial
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                SkeletonCard(modifier = Modifier.weight(1f))
                                SkeletonCard(modifier = Modifier.weight(1f))
                            }
                        }
                        is UiState.Error -> {
                            Text("Error cargando productos", modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.error)
                        }
                        is UiState.Success -> {
                            val products = state.data
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                                val rows = products.chunked(2)
                                rows.forEachIndexed { rowIndex, rowItems ->
                                    // DETECCIÓN DE SCROLL: Si pintamos la penúltima fila, pedimos más
                                    if (rowIndex >= rows.size - 2) {
                                        SideEffect { feedViewModel.loadNextPage() }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        rowItems.forEach { dto ->
                                            ProductCardLocal(
                                                product = dto,
                                                modifier = Modifier.weight(1f),
                                                onViewDetail = { id ->
                                                    navController.navigate(AppScreens.ProductDetail.createRoute(id))
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
                        else -> {}
                    }
                }

                // LOADER INFERIOR (PAGINACIÓN)
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}