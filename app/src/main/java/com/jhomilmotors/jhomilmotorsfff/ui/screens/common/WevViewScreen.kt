package com.jhomilmotors.jhomilmotorsfff.ui.screens.common

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.ContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    navController: NavController,
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
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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

            // --- BOTÓN ACEPTAR
            Button(
                onClick = {
                    // 1. "Escribimos" en el buzón de la pantalla anterior
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("tyc_accepted", true) // La clave es "tyc_accepted"

                    // 2. Cerramos esta pantalla
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD25419) // Un azul más serio o el Primary de tu tema
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("LEÍDO Y ACEPTAR", fontWeight = FontWeight.Bold)
            }

        }

    }
}