package com.jhomilmotors.jhomilmotorsfff.ui.screens.profile

import android.content.Context
import android.net.Uri
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp

import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

// ========== FUNCIONES DE INTENTS ==========
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

// ========== COMPONENTES REUTILIZABLES ==========

@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileHeaderCard(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTextField(
    label: String,
    value: String?,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ContactOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

// ========== PANTALLA PRINCIPAL ==========

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    logoutViewModel: AuthLogoutViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val logoutState by logoutViewModel.logoutState.collectAsState()
    val sessionValid by sessionViewModel.sessionValid.collectAsState()
    val isChecking by sessionViewModel.isChecking.collectAsState()

    val name by viewModel.name.collectAsState()
    val lastname by viewModel.lastname.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val address by viewModel.address.collectAsState()
    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    var isEditMode by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val supportPhone = "51922309105"
    val supportEmail = "fatima.rodriguez@tecsup.edu.pe"
    val whatsappMessage = "Hola Jhomil Motors, necesito ayuda."

    // ===== EFECTOS DE SESIÓN Y ESTADOS =====
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        sessionViewModel.checkSession()
    }

    LaunchedEffect(sessionValid, isChecking) {
        if (!isChecking && !sessionValid) {
            navController.navigate(AppScreens.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(logoutState) {
        when (logoutState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("Sesión cerrada correctamente")
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

    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                isEditMode = false
                viewModel.resetUpdateState()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
        }
    }

    // ===== UI PRINCIPAL =====
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        shape = MaterialTheme.shapes.medium
                    )
                }
            )
        },
        topBar = {
            ProfileTopBar(onBackClick = { navController.popBackStack() })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                isChecking || !sessionValid -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                else -> {
                    when (val state = profileState) {
                        is UiState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        is UiState.Success -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                // HEADER CON AVATAR Y DATOS PRINCIPALES
                                ProfileHeaderCard(
                                    name = "$name $lastname",
                                    email = email
                                )

                                // INFORMACIÓN PERSONAL
                                SectionTitle(text = "Información Personal")

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = MaterialTheme.shapes.large,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        ModernTextField(
                                            label = "Nombres",
                                            value = name,
                                            onValueChange = viewModel::onNameChange,
                                            enabled = isEditMode && updateState !is UiState.Loading
                                        )
                                        ModernTextField(
                                            label = "Apellidos",
                                            value = lastname,
                                            onValueChange = viewModel::onLastnameChange,
                                            enabled = isEditMode && updateState !is UiState.Loading
                                        )
                                        ModernTextField(
                                            label = "Correo electrónico",
                                            value = email,
                                            onValueChange = {},
                                            enabled = false,
                                            readOnly = true
                                        )
                                        ModernTextField(
                                            label = "Teléfono",
                                            value = phoneNumber,
                                            onValueChange = viewModel::onPhoneNumberChange,
                                            enabled = isEditMode && updateState !is UiState.Loading
                                        )
                                        ModernTextField(
                                            label = "Dirección",
                                            value = address,
                                            onValueChange = viewModel::onAddressChange,
                                            enabled = isEditMode && updateState !is UiState.Loading
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // BOTONES DE ACCIÓN
                                AnimatedVisibility(
                                    visible = !isEditMode,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        ActionButton(
                                            text = "Editar Perfil",
                                            icon = Icons.Default.Edit,
                                            onClick = { isEditMode = true },
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                AnimatedVisibility(
                                    visible = isEditMode,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        ActionButton(
                                            text = if (updateState is UiState.Loading) "Guardando..." else "Guardar Cambios",
                                            Icons.Default.Check,
                                            onClick = { viewModel.saveProfile() },
                                            enabled = updateState !is UiState.Loading,
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                        OutlinedButton(
                                            onClick = {
                                                isEditMode = false
                                                viewModel.loadProfile()
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp),
                                            shape = MaterialTheme.shapes.medium,
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            Text(
                                                text = "Cancelar",
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // SOPORTE Y CONTACTO
                                SectionTitle(text = "Soporte y Contacto")

                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ContactOptionCard(
                                        icon = Icons.Default.Phone,
                                        title = "Llamar a Soporte",
                                        subtitle = "Línea de atención al cliente",
                                        onClick = { callSupport(context, supportPhone) }
                                    )
                                    ContactOptionCard(
                                        icon = Icons.Default.Send,
                                        title = "WhatsApp",
                                        subtitle = "Chatea con nosotros",
                                        onClick = { contactViaWhatsapp(context, supportPhone, whatsappMessage) }
                                    )
                                    ContactOptionCard(
                                        icon = Icons.Default.Email,
                                        title = "Correo Electrónico",
                                        subtitle = "Envíanos un mensaje",
                                        onClick = { sendEmailToSupport(context, supportEmail, "Consulta desde la App") }
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // CERRAR SESIÓN
                                Divider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                ActionButton(
                                    text = if (logoutState is UiState.Loading) "Cerrando sesión..." else "Cerrar Sesión",
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    onClick = { logoutViewModel.logout() },
                                    enabled = logoutState !is UiState.Loading && !isEditMode,
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                        is UiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "Error al cargar el perfil",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = state.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Button(
                                        onClick = { viewModel.loadProfile() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    JhomilMotorsShopTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
