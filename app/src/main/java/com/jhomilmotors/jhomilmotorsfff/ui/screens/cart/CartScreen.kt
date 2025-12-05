package com.jhomilmotors.jhomilmotorsfff.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.cart.CartDTO
import com.jhomilmotors.jhomilmotorsfff.data.model.cart.CartItemDTO
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.cartState.collectAsState()
    val scrollState = rememberScrollState()

    // Recargar carrito al entrar
    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // MOSTRAR RESUMEN SOLO SI HAY DATOS Y EL CARRITO NO ESTÁ VACÍO
            if (state is UiState.Success && (state as UiState.Success).data.items.isNotEmpty()) {
                val cart = (state as UiState.Success<CartDTO>).data
                IntegratedCartSummary(
                    cart = cart,
                    onCheckout = {
                        navController.navigate(AppScreens.ConfirmationScreen.route)
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val uiState = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.ShoppingCart, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Hubo un problema al cargar el carrito", color = MaterialTheme.colorScheme.error)
                        Text(uiState.message, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCart() }) { Text("Reintentar") }
                    }
                }
                is UiState.Success -> {
                    val cart = uiState.data
                    if (cart.items.isEmpty()) {
                        EmptyCartView(onGoShopping = { navController.popBackStack() })
                    } else {
                        // CONTENIDO DEL CARRITO
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "${cart.items.size} productos",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            // LISTA DE PRODUCTOS
                            cart.items.forEach { item ->
                                CartItemView(
                                    item = item,
                                    onIncrease = { viewModel.updateItemQuantity(item.id, item.cantidad + 1) },
                                    onDecrease = { viewModel.updateItemQuantity(item.id, item.cantidad - 1) },
                                    onRemove = { viewModel.removeItem(item.id) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // Espacio extra al final para que la barra inferior no tape contenido
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

// ----------------------------------------------------------------------
// VISTA: ÍTEM INDIVIDUAL (DISEÑO MEJORADO)
// ----------------------------------------------------------------------
@Composable
fun CartItemView(
    item: CartItemDTO,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. IMAGEN (USANDO ASYNC IMAGE PARA URLS)
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.productoNombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit, // Fit para que se vea todo el producto
                    // Placeholder mientras carga o si falla
                    error = rememberVectorPainter(Icons.Default.ShoppingCart),
                    placeholder = rememberVectorPainter(Icons.Default.ShoppingCart)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. INFORMACIÓN Y CONTROLES
            Column(modifier = Modifier.weight(1f)) {
                // Título y Botón Eliminar en la misma fila superior
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.productoNombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(24.dp).offset(x = 4.dp, y = (-4).dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }

                // Variante (Color, Talla, etc)
                if (!item.varianteSku.isNullOrEmpty()) {
                    Text(
                        text = "SKU: ${item.varianteSku}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fila Inferior: Precio y Cantidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "S/ ${String.format("%.2f", item.precioUnitario)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Selector de Cantidad Estilizado
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    ) {
                        IconButton(
                            onClick = onDecrease,
                            enabled = item.cantidad > 1,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "-", modifier = Modifier.size(16.dp))
                        }

                        Text(
                            text = "${item.cantidad}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        val maxReached = item.cantidad >= item.stockDisponible
                        IconButton(
                            onClick = onIncrease,
                            enabled = !maxReached,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "+",
                                modifier = Modifier.size(16.dp),
                                tint = if(maxReached) Color.Gray else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Mensaje Stock Bajo
                if (item.stockDisponible <= 5) {
                    Text(
                        text = "¡Solo quedan ${item.stockDisponible}!",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// VISTA: RESUMEN FINAL (TOTALES)
// ----------------------------------------------------------------------
@Composable
fun IntegratedCartSummary(
    cart: CartDTO,
    onCheckout: () -> Unit
) {
    Surface(
        shadowElevation = 16.dp,
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            SummaryRow(label = "Subtotal", value = cart.subtotal)

            if (cart.descuentoTotal > 0) {
                SummaryRow(
                    label = "Descuentos",
                    value = -cart.descuentoTotal,
                    isHighlight = true,
                    highlightColor = Color(0xFF4CAF50) // Verde éxito
                )
            }

            if (cart.costoEnvio > 0) {
                SummaryRow(label = "Envío", value = cart.costoEnvio)
            } else {
                // Envío Gratis visual
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Envío", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("GRATIS", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total a Pagar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "S/ ${String.format("%.2f", cart.total)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("PROCESAR COMPRA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: Double,
    isHighlight: Boolean = false,
    highlightColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = "S/ ${String.format("%.2f", value)}",
            fontWeight = FontWeight.SemiBold,
            color = if (isHighlight) highlightColor else MaterialTheme.colorScheme.onSurface
        )
    }
}

// ----------------------------------------------------------------------
// VISTA: CARRITO VACÍO
// ----------------------------------------------------------------------
@Composable
fun EmptyCartView(onGoShopping: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Parece que aún no has añadido nada.\nExplora nuestro catálogo y encuentra lo mejor para tu moto.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGoShopping,
            modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Icon(Icons.Default.Store, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ir a la Tienda")
        }
    }
}