package com.jhomilmotors.jhomilmotorsfff.ui.screens.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.order.OrderResponse
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.history.HistoryViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// --- ENUMS ---
// Mapeamos los estados que devuelve tu Backend a colores
enum class OrderStatus(val displayName: String, val textColor: Color, val backgroundColor: Color) {
    PENDIENTE("Pendiente", Color(0xFFFFA500), Color(0xFFFFF8E1)),
    PAGADO("Pagado", Color(0xFF3E6AD3), Color(0xFFE3F2FD)), // Agregado
    ENVIADO("Enviado", Color(0xFF3E6AD3), Color(0xFFE3F2FD)),
    ENTREGADO("Entregado", Color(0xFF008000), Color(0xFFE8F5E9)),
    CANCELADO("Cancelado", Color(0xFFD32F2F), Color(0xFFFFEBEE)),
    UNKNOWN("Desconocido", Color.Gray, Color.LightGray);

    companion object {
        fun fromString(status: String): OrderStatus {
            return try {
                valueOf(status.uppercase())
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}

// --- COMPONENTES ---

@Composable
private fun OrderStatusChip(status: String) {
    val uiStatus = OrderStatus.fromString(status)
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(uiStatus.backgroundColor)
            .border(1.dp, uiStatus.textColor, MaterialTheme.shapes.extraLarge)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = uiStatus.displayName,
            color = uiStatus.textColor,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun OrderCard(order: OrderResponse, onClick: () -> Unit) {
    // Formatear fecha (Backend suele mandar ISO: 2025-12-05T10:00:00)
    val dateStr = try {
        // Intenta formatear si viene en ISO, sino muestra como viene
        // Nota: Para versiones viejas de Android usa SimpleDateFormat
        order.fechaPedido.replace("T", " ").take(16)
    } catch (e: Exception) { order.fechaPedido }

    // Imagen: Usamos la del primer item si existe, sino un placeholder
    val imageUrl = order.items.firstOrNull()?.imageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, MaterialTheme.shapes.large)
            .clickable { onClick() }
            .height(130.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen Dinámica
            AsyncImage(
                model = imageUrl,
                contentDescription = "Producto",
                placeholder = painterResource(R.drawable.logo_jhomil), // Tu logo como placeholder
                error = painterResource(R.drawable.logo_jhomil),
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = dateStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "Pedido #${order.id}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Total: S/${String.format("%.2f", order.total)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                OrderStatusChip(status = order.estado)
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right), // Asegúrate de tener este icono o usa Icons.AutoMirrored.Filled.ArrowForward
                    contentDescription = "Ver detalle",
                    tint = Color.Gray
                )
            }
        }
    }
}

// --- PANTALLA PRINCIPAL ---

@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    // 1. Obtener estado del ViewModel
    val state by viewModel.ordersState.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(2) } // Por defecto "Todos"
    val tabTitles = listOf("En Curso", "Completados", "Todos")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Mis Pedidos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp)) // Balancear el icono de atrás
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // TABS
            TabRow(selectedTabIndex = selectedTabIndex, containerColor = MaterialTheme.colorScheme.background) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CONTENIDO
            when (val uiState = state) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ocurrió un error", color = MaterialTheme.colorScheme.error)
                            Button(onClick = { viewModel.loadOrders() }) { Text("Reintentar") }
                        }
                    }
                }
                is UiState.Success -> {
                    val allOrders = uiState.data

                    // Lógica de Filtrado en Frontend
                    val filteredOrders = when (selectedTabIndex) {
                        0 -> allOrders.filter { it.estado == "PENDIENTE" || it.estado == "PAGADO" || it.estado == "ENVIADO" }
                        1 -> allOrders.filter { it.estado == "ENTREGADO" }
                        else -> allOrders
                    }

                    if (filteredOrders.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tienes pedidos en esta categoría.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onClick = {
                                        // TODO: Navegar al detalle del pedido (OrderTrackingScreen)
                                        // navController.navigate("order_detail/${order.id}")
                                    }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}