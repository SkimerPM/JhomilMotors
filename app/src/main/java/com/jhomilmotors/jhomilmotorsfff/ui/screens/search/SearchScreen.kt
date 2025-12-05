package com.jhomilmotors.jhomilmotorsfff.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jhomilmotors.jhomilmotorsfff.data.model.SearchResult
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val state by viewModel.searchState.collectAsState()

    // Foco automático para que el teclado salga al entrar
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = { /* La búsqueda es reactiva, no necesitamos acción extra */ },
                active = true, // Siempre expandido
                onActiveChange = { if (!it) onBack() }, // Si cierra, volver atrás
                placeholder = { Text("Buscar productos...", color = MaterialTheme.colorScheme.onBackground) },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Borrar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester) // Asignar foco
            ) {
                // CONTENIDO DENTRO DE LA BÚSQUEDA
                when (val uiState = state) {
                    is UiState.Loading -> {
                        Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Error -> {
                        if (query.isNotEmpty()) {
                            Text(
                                text = uiState.message,
                                modifier = Modifier.padding(16.dp),
                                color = Color.Gray
                            )
                        }
                    }
                    is UiState.Success -> {
                        LazyColumn {
                            items(uiState.data) { product ->
                                SearchResultItem(product, onClick = { onNavigateToDetail(product.id) })
                            }
                        }
                    }
                    else -> {
                        // Estado inicial (historial o sugerencias)
                        Column(Modifier.padding(16.dp)) {
                            Text("Sugerencias populares", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Text("Cascos", Modifier.padding(8.dp).clickable { viewModel.onQueryChange("Casco") })
                            Text("Aceites", Modifier.padding(8.dp).clickable { viewModel.onQueryChange("Aceite") })
                        }
                    }
                }
            }
        }
    ) { padding ->
        // Contenido manejado por SearchBar
        Box(modifier = Modifier.padding(padding))
    }
}

@Composable
fun SearchResultItem(product: SearchResult, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(product.nombre) },
        leadingContent = {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
        },
        modifier = Modifier.clickable { onClick() }
    )
    Divider()
}