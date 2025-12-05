package com.jhomilmotors.jhomilmotorsfff.ui.screens.DetailsProduct

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductDetailViewModel

// ----------------------------------------------------------------------
// 1. LÓGICA DE COMPARTIR (Intent Implícito)
// ----------------------------------------------------------------------

fun shareProduct(context: Context, product: ProductDetailsDto) {
    val shareText = """
        ¡Mira lo que encontré en Jhomil Motors! 🏍️
        
        *${product.nombre}*
        
        Precio: S/${String.format("%.2f", product.precioBase)}
        
        Entra a la app para ver más detalles.
    """.trimIndent()

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Jhomil Motors: ${product.nombre}")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir producto vía...")
    context.startActivity(shareIntent)
}

// ----------------------------------------------------------------------
// 2. COMPONENTES VISUALES
// ----------------------------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfessionalImageCarousel(
    images: List<ProductDetailsDto.ImagenResponse>,
    onImageClick: (String) -> Unit
) {
    if (images.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin imagen disponible", color = Color.Gray)
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color.White)
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                val imageUrl = images[page].url
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen producto",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onImageClick(imageUrl) },
                    contentScale = ContentScale.Fit
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom",
                    modifier = Modifier.padding(8.dp).size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outlineVariant
                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(if (pagerState.currentPage == iteration) 10.dp else 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Zoom Completo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
            }
        }
    }
}

@Composable
fun PriceAndPromoSection(
    price: Double,
    originalPrice: Double?,
    hasDiscount: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "S/ ${String.format("%.2f", price)}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.primary
            )

            if (hasDiscount && originalPrice != null && originalPrice > price) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "S/ ${String.format("%.2f", originalPrice)}",
                    style = MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                val discountPercent = ((originalPrice - price) / originalPrice * 100).toInt()
                Surface(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "-$discountPercent%",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
        Text(
            text = "Incluye IGV",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalVariantSelector(
    variantes: List<ProductDetailsDto.VarianteResponse>,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    Column {
        Text(
            text = "Elige una opción:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            variantes.forEach { variante ->
                val isSelected = variante.id == selectedId
                val label = variante.atributos.firstOrNull()?.valorTexto
                    ?: variante.atributos.firstOrNull()?.valorNumerico?.toString()
                    ?: variante.sku

                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(variante.id) },
                    label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun ProductSection(title: String, isOpenDefault: Boolean = false, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(isOpenDefault) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        if (expanded) {
            content()
            Spacer(modifier = Modifier.height(8.dp))
        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

@Composable
fun ProductAttributesList(attributes: List<ProductDetailsDto.AtributoResponse>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        attributes.forEach { attr ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = attr.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                val valor = attr.valorTexto ?: attr.valorNumerico?.toString() ?: "-"
                Text(
                    text = "$valor ${attr.unidad ?: ""}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        }
    }
}

// ----------------------------------------------------------------------
// 3. CONTENIDO PRINCIPAL (LOGICA CONECTADA)
// ----------------------------------------------------------------------

@Composable
fun ProductDetailContent(
    details: ProductDetailsDto,
    onBack: () -> Unit,
    onAddToCart: (Long, Int) -> Unit, // <--- Nueva función para conectar al padre
    cartState: UiState<Boolean>,      // <--- Estado de carga del carrito
    snackbarHostState: SnackbarHostState // <--- Para mostrar mensajes
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // --- VARIANTES ---
    var selectedVariantId by remember { mutableLongStateOf(details.variantes.firstOrNull()?.id ?: -1L) }
    val activeVariant = details.variantes.find { it.id == selectedVariantId }

    // --- STOCK ---
    val currentStock = activeVariant?.stock ?: 0

    // --- CONTADOR ---
    var quantity by remember { mutableIntStateOf(1) }
    LaunchedEffect(selectedVariantId) {
        quantity = if (currentStock > 0) 1 else 0
    }

    // --- PRECIOS & UI ---
    val currentPrice = activeVariant?.precio ?: details.precioBase
    val originalPrice = if (details.precioBase > currentPrice) details.precioBase else null
    val hasDiscount = originalPrice != null
    val activeImages = if (activeVariant != null && activeVariant.imagenes.isNotEmpty()) {
        activeVariant.imagenes
    } else {
        details.imagenes
    }

    // --- ZOOM ---
    var zoomImage by remember { mutableStateOf<String?>(null) }
    if (zoomImage != null) {
        FullScreenImageDialog(imageUrl = zoomImage!!, onDismiss = { zoomImage = null })
    }

    // Detectar si estamos añadiendo (para bloquear botones)
    val isAddingToCart = cartState is UiState.Loading

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }, // <--- AQUÍ SE MUESTRAN LOS MENSAJES
        bottomBar = {
            Surface(
                shadowElevation = 16.dp,
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SELECTOR DE CANTIDAD
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(
                                1.dp,
                                if (currentStock > 0) MaterialTheme.colorScheme.outline else Color.LightGray,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                            .alpha(if (currentStock > 0) 1f else 0.5f)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = currentStock > 0 && !isAddingToCart, // Bloqueado si carga
                            modifier = Modifier.size(32.dp)
                        ) { Icon(Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(16.dp)) }

                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = if (currentStock > 0) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )

                        IconButton(
                            onClick = { if (quantity < currentStock) quantity++ },
                            enabled = currentStock > 0 && quantity < currentStock && !isAddingToCart,
                            modifier = Modifier.size(32.dp)
                        ) { Icon(Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(16.dp)) }
                    }

                    // BOTÓN DE ACCIÓN
                    Button(
                        onClick = {
                            if (currentStock > 0 && !isAddingToCart) {
                                // LLAMADA REAL A LA FUNCIÓN DEL PADRE
                                onAddToCart(selectedVariantId, quantity)
                            }
                        },
                        enabled = currentStock > 0 && !isAddingToCart,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        if (isAddingToCart) {
                            // LOADING SPINNER
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            if (currentStock > 0) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Agregar al Carrito", fontWeight = FontWeight.Bold)
                            } else {
                                Text("AGOTADO", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { shareProduct(context, details) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // CARRUSEL
            ProfessionalImageCarousel(
                images = activeImages,
                onImageClick = { url -> zoomImage = url }
            )

            // INFO
            Column(modifier = Modifier.padding(24.dp)) {

                Text(
                    text = details.marcaNombre?.uppercase() ?: "JHOMIL MOTORS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = details.nombre,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                PriceAndPromoSection(
                    price = currentPrice,
                    originalPrice = originalPrice,
                    hasDiscount = hasDiscount
                )

                Spacer(modifier = Modifier.height(8.dp))
                if (currentStock > 0) {
                    val colorStock = if (currentStock <= 5) Color(0xFFD32F2F) else Color(0xFF388E3C)
                    val textoStock = if (currentStock <= 5) "¡Solo quedan $currentStock!" else "Stock disponible: $currentStock"

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(colorStock))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = textoStock,
                            style = MaterialTheme.typography.labelMedium,
                            color = colorStock,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "Actualmente no disponible",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (details.variantes.isNotEmpty()) {
                    ProfessionalVariantSelector(
                        variantes = details.variantes,
                        selectedId = selectedVariantId,
                        onSelect = { selectedVariantId = it }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                ProductSection(title = "Descripción", isOpenDefault = true) {
                    Text(
                        text = details.descripcion ?: "Sin descripción detallada.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }

                ProductSection(title = "Especificaciones Técnicas") {
                    val attrsToShow = activeVariant?.atributos ?: details.atributos
                    if (attrsToShow.isNotEmpty()) {
                        ProductAttributesList(attrsToShow)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("No hay especificaciones adicionales.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// 4. PANTALLA PRINCIPAL (ENTRY POINT) - AQUÍ CONECTAMOS TODO
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsProductoScreen(
    productId: Int,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // --- NUEVO: ESCUCHAMOS EL ESTADO DEL CARRITO ---
    val cartState by viewModel.addToCartState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto para mostrar el Snackbar cuando termina la operación
    LaunchedEffect(cartState) {
        when (cartState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar(
                    message = "✅ Producto añadido al carrito correctamente",
                    duration = SnackbarDuration.Short
                )
                viewModel.resetCartState() // Limpiamos estado
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = "❌ Error: ${(cartState as UiState.Error).message}",
                    duration = SnackbarDuration.Long
                )
                viewModel.resetCartState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (val currentState = state) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "¡Ups!", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.error)
                    Text(text = currentState.message, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { onBack() }, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Regresar")
                    }
                }
            }
            is UiState.Success -> {
                // AQUÍ PASAMOS LA FUNCIÓN REAL AL CONTENIDO
                ProductDetailContent(
                    details = currentState.data,
                    onBack = onBack,
                    onAddToCart = { variantId, quantity ->
                        // LOG REAL PARA DEPURACIÓN
                        println("🚀 ACTION: UI pidiendo añadir al carrito -> Variant: $variantId, Qty: $quantity")
                        viewModel.addToCart(variantId, quantity)
                    },
                    cartState = cartState,
                    snackbarHostState = snackbarHostState
                )
            }
            else -> {}
        }
    }
}