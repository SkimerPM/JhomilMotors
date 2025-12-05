package com.jhomilmotors.jhomilmotorsfff.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductResponse
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductListViewModel
import androidx.compose.foundation.lazy.grid.items
// ----------------------------------------------------------------------
// 1. LA PANTALLA CONTENEDORA (Stateful Wrapper)
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    categoryId: Int,
    onBack: () -> Unit,
    onNavigateToDetail: (productId: Int) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    // Observar el estado (StateFlow)
    val state by viewModel.products.collectAsState()

    // Cargar datos al entrar (Disparador único)
    LaunchedEffect(categoryId) {
        // La carga se inicia en el 'init' del VM, pero si queremos recargar por cambio de ID, se hace aquí:
        viewModel.loadProducts(categoryId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
        ) {
            when (val currentState = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Ocurrió un error", color = Color.Red)
                        Text(text = currentState.message, style = MaterialTheme.typography.bodySmall)
                    }
                }

                is UiState.Success -> {
                    if (currentState.data.isEmpty()) {
                        Text(
                            text = "No hay productos en esta categoría",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        // Pasamos la lista y la ACCIÓN de navegación a la cuadrícula
                        ProductGrid(
                            products = currentState.data,
                            onProductClick = onNavigateToDetail // ⬅️ Enlazamos el evento
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
fun ProductGrid(products: List<ProductResponse>, onProductClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            // Le pasamos el producto y la acción con su ID
            ProductCardItem(
                product = product,
                // Al hacer clic en la tarjeta, llamamos a la lambda superior
                onItemClick = { onProductClick(product.id.toInt()) }
            )
        }
    }
}

// ----------------------------------------------------------------------
// 3. TARJETA INDIVIDUAL (El Diseño)
// ----------------------------------------------------------------------

@Composable
fun ProductCardItem(product: ProductResponse, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // --- IMAGEN DEL PRODUCTO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imagenUrl,
                    contentDescription = product.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // --- DETALLES (Texto y Precio) ---
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Marca (si existe)
                product.marcaNombre?.let { marca ->
                    Text(
                        text = marca.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }

                // Nombre del producto
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio
                Text(
                    text = "S/ ${String.format("%.2f", product.precioBase)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}