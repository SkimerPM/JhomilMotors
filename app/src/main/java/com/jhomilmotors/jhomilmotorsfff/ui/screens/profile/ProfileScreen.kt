package com.jhomilmotors.jhomilmotorsfff.ui.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.AuthLogoutViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.ProfileViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SessionViewModel

// --- LÓGICA DE INTENTS IMPLÍCITOS ---

fun callSupport(context: Context, phoneNumber: String) {
    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    if (dialIntent.resolveActivity(context.packageManager) != null) {
        ContextCompat.startActivity(context, dialIntent, null)
    }
}

fun contactViaWhatsapp(context: Context, phoneNumber: String, message: String) {
    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
    val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    if (whatsappIntent.resolveActivity(context.packageManager) != null) {
        ContextCompat.startActivity(context, whatsappIntent, null)
    }
}

fun sendEmailToSupport(context: Context, emailAddress: String, subject: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    if (emailIntent.resolveActivity(context.packageManager) != null) {
        ContextCompat.startActivity(context, emailIntent, null)
    }
}

// --- COMPONENTE DE OPCIÓN DE CONTACTO ---

@Composable
fun ContactOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.LightGray
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.LightGray)
}

// --- OTROS COMPONENTES ---

@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .background(gradient, shape = MaterialTheme.shapes.medium)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        )
    }
}

// --- PANTALLA PRINCIPAL CON HILT Y MVVM ---

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    logoutViewModel: AuthLogoutViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // ===== Estados de logout =====
    val logoutState by logoutViewModel.logoutState.collectAsState()

    // ===== Estados de sesión =====
    val sessionValid by sessionViewModel.sessionValid.collectAsState()
    val isChecking by sessionViewModel.isChecking.collectAsState()

    // ===== Estados de perfil =====
    val name by viewModel.name.collectAsState()
    val lastname by viewModel.lastname.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val address by viewModel.address.collectAsState()

    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Datos de soporte
    val supportPhone = "51922309105"
    val supportEmail = "fatima.rodriguez@tecsup.edu.pe"
    val whatsappMessage = "Hola Jhomil Motors, necesito ayuda con mi pedido."

    val primaryGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF20376D), Color(0xFF3E6AD3))
    )
    val errorGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF7F1736), Color(0xFFCB1332))
    )

    // ===== Validar sesión al entrar =====
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        sessionViewModel.checkSession()
    }

    // ===== Redirigir a login si sesión no es válida =====
    LaunchedEffect(sessionValid, isChecking) {
        if (!isChecking && !sessionValid) {
            navController.navigate(AppScreens.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // ===== Manejar logout exitoso =====
    LaunchedEffect(logoutState) {
        when (logoutState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("¡Sesión cerrada!")
                navController.navigate(AppScreens.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                logoutViewModel.resetLogoutState()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar((logoutState as UiState.Error).message)
            }
            else -> {}
        }
    }

    // ===== Manejar actualización de perfil =====
    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("¡Perfil actualizado!")
                viewModel.resetUpdateState()
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
            when {
                isChecking -> {
                    // Validando sesión
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                !sessionValid -> {
                    // Sesión inválida (se redirige automáticamente en LaunchedEffect)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    // Sesión válida, mostrar contenido
                    when (val state = profileState) {
                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is UiState.Success -> {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // --- Barra Superior ---
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Volver",
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(44.dp)
                                        )
                                    }
                                    Text(
                                        text = "Mi Perfil",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.width(48.dp))
                                }

                                // --- Contenido Principal Desplazable ---
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 24.dp, vertical = 16.dp)
                                ) {
                                    // --- Botón de Editar ---
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        OutlinedButton(
                                            onClick = { /* Toggle edit mode */ },
                                            enabled = updateState !is UiState.Loading
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_edit),
                                                contentDescription = "Editar",
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "Editar")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // --- Campos del Formulario ---
                                    ProfileTextField(
                                        label = "Nombres",
                                        value = name,
                                        onValueChange = viewModel::onNameChange,
                                        enabled = updateState !is UiState.Loading
                                    )
                                    ProfileTextField(
                                        label = "Apellidos",
                                        value = lastname,
                                        onValueChange = viewModel::onLastnameChange,
                                        enabled = updateState !is UiState.Loading
                                    )
                                    ProfileTextField(
                                        label = "Correo electrónico",
                                        value = email,
                                        onValueChange = {},
                                        enabled = false
                                    )
                                    ProfileTextField(
                                        label = "Teléfono",
                                        value = phoneNumber,
                                        onValueChange = viewModel::onPhoneNumberChange,
                                        enabled = updateState !is UiState.Loading
                                    )
                                    ProfileTextField(
                                        label = "Dirección",
                                        value = address,
                                        onValueChange = viewModel::onAddressChange,
                                        enabled = updateState !is UiState.Loading
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))

                                    // --- Botones de Acción ---
                                    GradientButton(
                                        text = if (updateState is UiState.Loading) "Guardando..." else "Guardar Cambios",
                                        gradient = primaryGradientBrush,
                                        enabled = updateState !is UiState.Loading,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(62.dp),
                                        onClick = { viewModel.saveProfile() }
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    GradientButton(
                                        text = if (logoutState is UiState.Loading) "Cerrando..." else "Cerrar Sesión",
                                        gradient = errorGradientBrush,
                                        enabled = logoutState !is UiState.Loading,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(62.dp),
                                        onClick = { logoutViewModel.logout() }
                                    )

                                    // --- SECCIÓN SOPORTE Y CONTACTO ---
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = "Soporte y Contacto",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    ContactOptionItem(
                                        icon = Icons.Default.Phone,
                                        label = "Llamar a Soporte (Línea Fija)",
                                        onClick = { callSupport(context, supportPhone) }
                                    )

                                    ContactOptionItem(
                                        icon = Icons.Default.Send,
                                        label = "Contactar por WhatsApp",
                                        onClick = { contactViaWhatsapp(context, supportPhone, whatsappMessage) }
                                    )

                                    ContactOptionItem(
                                        icon = Icons.Default.Email,
                                        label = "Enviar Correo Electrónico",
                                        onClick = { sendEmailToSupport(context, supportEmail, "Consulta desde la App Móvil") }
                                    )

                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            }
                        }

                        is UiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Error: ${state.message}",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { viewModel.loadProfile() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    JhomilMotorsShopTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
