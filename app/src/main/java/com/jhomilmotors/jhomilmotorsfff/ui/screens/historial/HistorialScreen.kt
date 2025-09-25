package com.jhomilmotors.jhomilmotorsfff.ui.screens.historial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

// --- Modelos de Datos (Solo para el diseño, luego irán en la capa de datos) ---

enum class OrderStatus(val displayName: String, val textColor: Color, val backgroundColor: Color) {
    PENDIENTE("Pendiente", Color(0xFFFFA500), Color(0xFFFFF8E1)),
    ENVIADO("Enviado", Color(0xFF3E6AD3), Color(0xFFE3F2FD)),
    ENTREGADO("Entregado", Color(0xFF008000), Color(0xFFE8F5E9))
}

data class Order(
    val id: String,
    val timestamp: String,
    val total: String,
    val status: OrderStatus,
    val imageUrl: Int // Usamos Int para el ID del drawable de momento
)

//micro composables
@Composable
private fun OrderStatusChip(status: OrderStatus) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(status.backgroundColor)
            .border(1.dp, status.textColor, MaterialTheme.shapes.extraLarge)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            color = status.textColor,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp, // El tamaño de la sombra
                shape = MaterialTheme.shapes.large, // La misma forma que la Card
                spotColor = MaterialTheme.colorScheme.onBackground, // Color principal de la sombra
                ambientColor = MaterialTheme.colorScheme.onBackground // Color ambiental de la sombra
            )
            .clickable { onClick() }
            .height(130.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.padding(14.dp) .height(130.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = order.imageUrl),
                contentDescription = "Imagen de producto",
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = order.timestamp, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Pedido ${order.id}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Total: ${order.total}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(74.dp)
            ) {
                OrderStatusChip(status = order.status)
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right), // Necesitarás este ícono
                    contentDescription = "Ver detalle",
                    tint = Color(0xFF8C8C8C)
                )
            }
        }
    }
}

//principal
@Composable
fun HistorialScreen(navController: NavController) {
    // --- Datos de Ejemplo (Hardcoded) ---
    // En el futuro, estos datos vendrán de un ViewModel.
    val sampleOrders = listOf(
        Order("#JM-0001", "15/09/2025 - 12:35 p.m.", "S/320.00", OrderStatus.PENDIENTE, R.drawable.bateria_enerjet),
        Order("#JM-0002", "14/09/2025 - 12:47 p.m.", "S/240.00", OrderStatus.ENVIADO, R.drawable.bateria_enerjet),
        Order("#JM-0003", "10/09/2025 - 03:51 p.m.", "S/24.00", OrderStatus.ENTREGADO, R.drawable.bateria_enerjet)
    )

    // --- Estado de la UI (Manejado localmente con remember) ---
    // En el futuro, este estado también lo manejará el ViewModel.
    var selectedTabIndex by remember { mutableStateOf(2) } // 0: En Curso, 1: Completados, 2: Todos
    val tabTitles = listOf("En Curso", "Completados", "Todos")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: navController.popBackStack() */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", modifier = Modifier.size(44.dp))
            }
            Text(
                text = "Mis Pedidos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // --- Pestañas de Filtro (TabRow) ---
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // --- Lógica de Filtrado (directamente en la UI por ahora) ---
        val filteredOrders = when (selectedTabIndex) {
            0 -> sampleOrders.filter { it.status == OrderStatus.ENVIADO || it.status == OrderStatus.PENDIENTE }
            1 -> sampleOrders.filter { it.status == OrderStatus.ENTREGADO }
            else -> sampleOrders // "Todos"
        }

        // --- Lista de Pedidos ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(filteredOrders) { order ->
                OrderCard(order = order, onClick = {  })
            }
        }
    }
}

@Preview(showBackground = true, )
@Composable
fun HistorialScreenPreview() {
    JhomilMotorsShopTheme {
        HistorialScreen(navController = rememberNavController())
    }
}