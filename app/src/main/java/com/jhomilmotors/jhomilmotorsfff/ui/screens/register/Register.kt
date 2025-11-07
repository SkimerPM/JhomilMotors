package com.jhomilmotors.jhomilmotorsfff.ui.screens.register

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.AuthViewModel

@Composable
fun Register(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val nombre by viewModel.nombre.collectAsState()
    val apellido by viewModel.apellido.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val registerState by viewModel.registerState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("¡Registro exitoso!")
                navController.navigate(AppScreens.Login.route) {
                    popUpTo(AppScreens.Register.route) { inclusive = true }
                }
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
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
                    .verticalScroll(rememberScrollState()),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.top_image_login),
                    contentDescription = "Imagen encabezado del Login",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
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
                        text = "Crea tu cuenta",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(17.dp))
                    Text(
                        text = "Regístrate para empezar a comprar en Jhomil Motors.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        text = "Nombre",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = nombre,
                        onValueChange = viewModel::onNombreChange,
                        placeholder = {
                            Text(
                                "introduce tu nombre aquí",
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
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        text = "Apellido",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = apellido,
                        onValueChange = viewModel::onApellidoChange,
                        placeholder = {
                            Text(
                                "introduce tu apellido aquí",
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
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        text = "Correo",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
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
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = {
                            Text(
                                "introduce una contraseña segura",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
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
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.registerUser() },
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
                        enabled = registerState !is UiState.Loading
                    ) {
                        if (registerState is UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.background
                            )
                        } else {
                            Text(
                                text = "Registrarse",
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Iniciar sesión",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                navController.navigate(AppScreens.Login.route)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { },
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
                }
            }
        }
    }
}
