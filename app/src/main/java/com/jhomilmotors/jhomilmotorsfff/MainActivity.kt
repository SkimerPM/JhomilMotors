package com.jhomilmotors.jhomilmotorsfff

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jhomilmotors.jhomilmotorsfff.navigation.AppNavigation
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest

// Función de utilidad para logs (se mantiene igual)
fun logAppSignature(context: Context) {
    try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
        }

        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo?.apkContentsSigners
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures
        }

        signatures?.forEach { signature ->
            val md = MessageDigest.getInstance("SHA-1")
            md.update(signature.toByteArray())
            val digest = md.digest()
            val hexString = StringBuilder()
            for (b in digest) {
                hexString.append(String.format("%02X:", b))
            }
            Log.e("FIRMA_REAL", "SHA-1 EN DISPOSITIVO: ${hexString.toString().dropLast(1)}")
        }
    } catch (e: Exception) {
        Log.e("FIRMA_REAL", "Error obteniendo firma", e)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el diseño de borde a borde (detrás de la barra de estado)
        enableEdgeToEdge()

        logAppSignature(this)

        setContent {
            JhomilMotorsShopTheme {
                // CORRECCIÓN: Eliminamos el Scaffold y Column globales.
                // AppEntry ocupa toda la pantalla y las pantallas hijas (como Home)
                // gestionarán su propio Scaffold.
                AppEntry()
            }
        }
    }
}

@Composable
fun AppEntry() {
    // Surface provee el color de fondo base correcto según el tema (Blanco/Negro)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavigation()
    }
}