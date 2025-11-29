package com.jhomilmotors.jhomilmotorsfff

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build // <--- IMPORTANTE
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jhomilmotors.jhomilmotorsfff.navigation.AppNavigation
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest

// --- PEGA AQUÍ LA FUNCIÓN CORREGIDA QUE TE DI ARRIBA ---
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
        enableEdgeToEdge()

        // Diagnóstico: Imprimir SHA-1
        logAppSignature(this)

        setContent {
            JhomilMotorsShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        AppEntry()
                    }
                }
            }
        }
    }
}

@Composable
fun AppEntry() {
    JhomilMotorsShopTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Poppins $name!", modifier = modifier)
}

@Composable
@Preview()
fun prev(){
    JhomilMotorsShopTheme{
        Greeting("Grupo JhomilMotors")
    }
}