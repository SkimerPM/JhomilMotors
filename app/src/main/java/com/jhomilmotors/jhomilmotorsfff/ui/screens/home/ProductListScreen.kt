package com.jhomilmotors.jhomilmotorsfff.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// IMPORTS DE ICONOS BONITOS (Requieren la librería Extended)
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductResponse
import com.jhomilmotors.jhomilmotorsfff.ui.components.JhomilLoader
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductListViewModel

// ==========================================
// UTILIDAD: BOUNCE CLICK
// ==========================================
enum class ListButtonState { Pressed, Idle }

fun Modifier.bounceClickList(scaleDown: Float = 0.95f, onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ListButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ListButtonState.Pressed) scaleDown else 1f, label = "bounce")

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
                buttonState = if (buttonState == ListButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ListButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ListButtonState.Pressed
                }
            }
        }
}

// ----------------------------------------------------------------------
// 1. LA PANTALLA CONTENEDORA
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    categoryId: Int,
    categoryName: String,
    onBack: () -> Unit,
    onNavigateToDetail: (productId: Int) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val state by viewModel.products.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.loadProducts(categoryId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Productos",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.shadow(elevation = 2.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is UiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        JhomilLoader(modifier = Modifier.size(150.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Buscando productos...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }

                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            // Icono: Imagen Rota (Outlined)
                            painter = rememberVectorPainter(Icons.Outlined.BrokenImage),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "¡Ups! Algo salió mal.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { viewModel.loadProducts(categoryId) }) {
                            Text("Reintentar")
                        }
                    }
                }

                is UiState.Success -> {
                    if (currentState.data.isEmpty()) {
                        // --- EMPTY STATE ---
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Image(
                                // Icono: Caja de inventario (Outlined)
                                painter = rememberVectorPainter(Icons.Outlined.Inventory2),
                                contentDescription = null,
                                modifier = Modifier.size(120.dp).alpha(0.5f),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay productos aquí",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Pronto añadiremos más novedades para esta categoría.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        ProductGrid(
                            products = currentState.data,
                            onProductClick = onNavigateToDetail,
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

// ----------------------------------------------------------------------
// 2. CUADRÍCULA (GRID)
// ----------------------------------------------------------------------

@Composable
fun ProductGrid(
    products: List<ProductResponse>,
    onProductClick: (Int) -> Unit,
    contentPadding: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductCardItem(
                product = product,
                onItemClick = { onProductClick(product.id.toInt()) }
            )
        }
    }
}

// ----------------------------------------------------------------------
// 3. TARJETA INDIVIDUAL
// ----------------------------------------------------------------------

@Composable
fun ProductCardItem(product: ProductResponse, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClickList { onItemClick() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column {
            // --- IMAGEN DEL PRODUCTO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imagenUrl,
                    contentDescription = product.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    // Iconos: Imagen (Outlined) y Error (Outlined)
                    placeholder = rememberVectorPainter(Icons.Outlined.Image),
                    error = rememberVectorPainter(Icons.Outlined.BrokenImage)
                )
            }

            // --- DETALLES ---
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.marcaNombre?.uppercase() ?: "JHOMIL MOTORS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "S/ ${String.format("%.2f", product.precioBase)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}