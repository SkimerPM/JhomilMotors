package com.jhomilmotors.jhomilmotorsfff.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.ui.screens.profile.ProfileScreen
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.AuthViewModel

@Composable
fun VerificationPendingScreen(
    email: String,
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isResending by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // O tu color de fondo
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Verifica tu Correo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hemos enviado un enlace de activación a:",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Text(
                text = email,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Revisa tu bandeja de entrada (y Spam). Haz clic en el enlace para activar tu cuenta y luego inicia sesión.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Volver al Login
            Button(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Volver al Inicio de Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Reenviar
            TextButton(
                onClick = {
                    if (!isResending) {
                        isResending = true
                        viewModel.resendVerificationEmail(email) { success, msg ->
                            isResending = false
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                enabled = !isResending
            ) {
                if (isResending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Text("¿No recibiste el correo? Reenviar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    JhomilMotorsShopTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
