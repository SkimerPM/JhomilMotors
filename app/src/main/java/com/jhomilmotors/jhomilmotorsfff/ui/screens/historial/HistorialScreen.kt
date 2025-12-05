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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.order.OrderResponse
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.history.HistoryViewModel

// --- 1. ENUM PARA COLORES Y TEXTOS ---
enum class OrderStatus(val displayName: String, val textColor: Color, val backgroundColor: Color) {
    PENDIENTE("Pendiente", Color(0xFFE65100), Color(0xFFFFF3E0)),    // Naranja
    PAGADO("Pagado", Color(0xFF1565C0), Color(0xFFE3F2FD)),         // Azul
    PREPARANDO("Preparando", Color(0xFF6A1B9A), Color(0xFFF3E5F5)), // Morado
    ENVIADO("Enviado", Color(0xFF0277BD), Color(0xFFE1F5FE)),       // Celeste
    ENTREGADO("Entregado", Color(0xFF2E7D32), Color(0xFFE8F5E9)),   // Verde
    CANCELADO("Cancelado", Color(0xFFC62828), Color(0xFFFFEBEE)),   // Rojo
    UNKNOWN("Desconocido", Color.Gray, Color.LightGray);

    companion object {
        fun fromString(status: String): OrderStatus {
            return try {
                // Convierte el string del backend (ej: "PENDIENTE") al Enum
                valueOf(status.uppercase())
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}

// --- 2. COMPONENTES VISUALES (Chip y Card) ---

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
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OrderCard(order: OrderResponse, onClick: () -> Unit) {
    val dateStr = if (order.fechaPedido.length >= 16) {
        order.fechaPedido.replace("T", " ").substring(0, 16)
    } else order.fechaPedido

    // Tomamos la primera imagen de los items, o null si no hay
    val imageUrl = order.items.firstOrNull()?.imageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, MaterialTheme.shapes.large)
            .clickable { onClick() }
            .height(135.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            AsyncImage(
                model = imageUrl,
                contentDescription = "Producto",
                placeholder = painterResource(R.drawable.logo_jhomil),
                error = painterResource(R.drawable.logo_jhomil),
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Gray.copy(alpha = 0.1f))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Textos
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Pedido #${order.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total: S/${String.format("%.2f", order.total)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Estado
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                OrderStatusChip(status = order.estado)
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right), // TU FLECHITA
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// --- 3. PANTALLA PRINCIPAL ---

@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.ordersState.collectAsState()

    // Índice de la pestaña seleccionada: 0=Pendientes, 1=Entregados, 2=Todos
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Pendientes", "Entregados", "Todos")

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                // Pestañas (Tabs)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (val uiState = state) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No pudimos cargar tus pedidos", color = MaterialTheme.colorScheme.error)
                            Button(onClick = { viewModel.loadOrders() }) { Text("Reintentar") }
                        }
                    }
                }
                is UiState.Success -> {
                    val allOrders = uiState.data

                    val filteredOrders = when (selectedTabIndex) {
                        0 -> allOrders.filter {
                            // Normalizamos el estado que viene del backend
                            val s = it.estado.uppercase().trim()

                            s == "PENDIENTE" ||
                                    s == "PAGADO" ||
                                    s == "PREPARANDO" ||
                                    s == "ENVIADO"
                        }
                        1 -> allOrders.filter {
                            val s = it.estado.uppercase().trim()
                            s == "ENTREGADO"
                        }
                        else -> allOrders // PESTAÑA TODOS
                    }

                    if (filteredOrders.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_orders), // Icono vacío
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "No hay pedidos aquí",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onClick = {
                                        // Aquí irías al detalle si lo tuvieras
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