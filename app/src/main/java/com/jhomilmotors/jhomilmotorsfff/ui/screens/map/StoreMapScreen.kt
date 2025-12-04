package com.jhomilmotors.jhomilmotorsfff.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(onBack: () -> Unit) {
    // COORDENADAS DE TU TIENDA (Ejemplo: Jhomil Motors real o ficticia)
    // Cambia esto por la lat/lng real.
    val storeLocation = LatLng(-7.4233101, -79.5064597)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(storeLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestra Ubicación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            Marker(
                state = MarkerState(position = storeLocation),
                title = "Jhomil Motors",
                snippet = "Tu tienda de confianza"
            )
        }
    }
}