package com.jhomilmotors.jhomilmotorsfff.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.AuthViewModel

@Composable
fun Login(
    navController: NavController,
    viewModel: AuthViewModel = viewModel() // ← Inyección automática del ViewModel
) {
    // Observar los estados del ViewModel
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    // SnackBar para mostrar errores
    val snackbarHostState = remember { SnackbarHostState() }

    // contexto
    val context = LocalContext.current

    // Manejar el estado de login
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is UiState.Success -> {
                // Login exitoso: navegar a home
                snackbarHostState.showSnackbar("¡Bienvenido!")
                navController.navigate(AppScreens.HomeScreen.route) {
                    // Limpiar el stack de navegación (no volver con back)
                    popUpTo(AppScreens.Login.route) { inclusive = true }
                }
            }
            is UiState.Error -> {
                // Mostrar error en Snackbar
                snackbarHostState.showSnackbar(state.message)
            }
            else -> { /* Idle o Loading, no hacer nada aquí */ }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // ← Scroll para teclado
            ) {
                Image(
                    painter = painterResource(id = R.drawable.top_image_login),
                    contentDescription = "Imagen encabezado del Login",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .clipToBounds(),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp),
                ) {
                    Text(
                        text = "Inicio de Sesión",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(17.dp))
                    Text(
                        text = "Inicia Sesión con tu cuenta de JhomilShop",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // ===== Campo Correo =====
                    Text(
                        text = "Correo",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email, // ← Estado del ViewModel
                        onValueChange = viewModel::onEmailChange, // ← Actualiza ViewModel
                        placeholder = {
                            Text(
                                "tu.email@ejemplo.com",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = loginState !is UiState.Loading // ← Deshabilitar en loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // ===== Campo Contraseña =====
                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password, // ← Estado del ViewModel
                        onValueChange = viewModel::onPasswordChange, // ← Actualiza ViewModel
                        placeholder = {
                            Text(
                                "introduce tu contraseña aquí",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(), // ← Ocultar texto
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = loginState !is UiState.Loading // ← Deshabilitar en loading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== Botón Iniciar Sesión =====
                    Button(
                        onClick = { viewModel.loginUser(context) }, // ← Llamar función del ViewModel
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(0.8f)
                                    ),
                                    startX = Float.POSITIVE_INFINITY,
                                    endX = 0f
                                ),
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .fillMaxWidth()
                            .height(62.dp),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        enabled = loginState !is UiState.Loading // ← Deshabilitar en loading
                    ) {
                        if (loginState is UiState.Loading) {
                            // Mostrar ProgressBar cuando está cargando
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== Divisor "o" =====
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Text(
                            text = "o",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== Botón Google (deshabilitado por ahora) =====
                    Button(
                        onClick = { /* TODO: Google Sign-In */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.googl_logo),
                            contentDescription = "Logo de Google",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== Link a Registro =====
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿No tienes una cuenta?",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Registrarse",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                navController.navigate(AppScreens.Register.route)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginScreen(){
    JhomilMotorsShopTheme{
        Login(navController = rememberNavController())
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewLoginScreenClaro(){
    JhomilMotorsShopTheme{
        Login(navController = rememberNavController())
    }
}
