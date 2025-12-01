package com.jhomilmotors.jhomilmotorsfff.ui.screens.common

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.ContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    codigo: String,
    onBack: () -> Unit,
    viewModel: ContentViewModel = hiltViewModel()
) {
    // Disparar la carga al iniciar
    LaunchedEffect(codigo) {
        viewModel.loadContent(codigo)
    }

    val state by viewModel.contentState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Información ($codigo)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val uiState = state) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text("Error: ${uiState.message}")
                is UiState.Success -> {
                    // Mostrar WebView con el HTML decodificado
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                            }
                        },
                        update = { webView ->
                            webView.loadDataWithBaseURL(null, uiState.data, "text/html", "UTF-8", null)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {}
            }
        }
    }
}