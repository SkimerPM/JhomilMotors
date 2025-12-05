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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    // Estado local del checkbox
    var isChecked by remember { mutableStateOf(false) }
    var hasReadTyC by remember { mutableStateOf(false) }

    // Lógica de retorno de TyC
    val navBackStackEntry = navController.currentBackStackEntry
    val acceptedTerms by navBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("tyc_accepted", false)
        ?.collectAsState() ?: remember { mutableStateOf(false) }

    LaunchedEffect(acceptedTerms) {
        if (acceptedTerms) {
            isChecked = true
            hasReadTyC = true
            navBackStackEntry?.savedStateHandle?.remove<Boolean>("tyc_accepted")
        }
    }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is UiState.Success -> {
                val emailRegistrado = viewModel.email.value
                navController.navigate(AppScreens.VerificationPending.createRoute(emailRegistrado)) {
                    popUpTo(AppScreens.Register.route) { inclusive = true }
                }
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
        }
    }

    RegisterContent(
        nombre = nombre,
        onNombreChange = viewModel::onNombreChange,
        apellido = apellido,
        onApellidoChange = viewModel::onApellidoChange,
        email = email,
        onEmailChange = viewModel::onEmailChange,
        password = password,
        onPasswordChange = viewModel::onPasswordChange,
        isChecked = isChecked,
        onCheckedChange = { isChecked = it },
        registerState = registerState,
        snackbarHostState = snackbarHostState,
        onRegisterClick = {
            scope.launch {
                if (nombre.isBlank() || apellido.isBlank() || email.isBlank() || password.isBlank()) {
                    snackbarHostState.showSnackbar("Todos los datos son necesarios")
                } else if (!hasReadTyC) {
                    snackbarHostState.showSnackbar("Debes entrar a leer los Términos y Condiciones")
                } else if (!isChecked) {
                    snackbarHostState.showSnackbar("Debes marcar la casilla para continuar")
                } else {
                    viewModel.registerUser()
                }
            }
        },
        onNavigateLogin = { navController.navigate(AppScreens.Login.route) },
        onNavigateTyC = { navController.navigate(AppScreens.WebViewContent.createRoute("TYC")) },
        onNavigatePrivacidad = { navController.navigate(AppScreens.WebViewContent.createRoute("PRIVACIDAD")) }
    )
}

@Composable
fun RegisterContent(
    nombre: String, onNombreChange: (String) -> Unit,
    apellido: String, onApellidoChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    isChecked: Boolean, onCheckedChange: (Boolean) -> Unit,
    registerState: UiState<UserResponse>,
    snackbarHostState: SnackbarHostState,
    onRegisterClick: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateTyC: () -> Unit,
    onNavigatePrivacidad: () -> Unit
) {
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

                    // INPUT NOMBRE
                    Text("Nombre", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        modifier = Modifier.fillMaxWidth().height(62.dp).testTag("input_nombre"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f), unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), cursorColor = MaterialTheme.colorScheme.primary, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // INPUT APELLIDO
                    Text("Apellido", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = apellido,
                        onValueChange = onApellidoChange,
                        modifier = Modifier.fillMaxWidth().height(62.dp).testTag("input_apellido"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f), unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), cursorColor = MaterialTheme.colorScheme.primary, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // INPUT EMAIL
                    Text("Correo", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth().height(62.dp).testTag("input_email"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f), unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), cursorColor = MaterialTheme.colorScheme.primary, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    // INPUT PASSWORD
                    Text("Contraseña", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().height(62.dp).testTag("input_password"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f), unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), cursorColor = MaterialTheme.colorScheme.primary, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        enabled = registerState !is UiState.Loading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = onCheckedChange,
                            modifier = Modifier.testTag("check_tyc")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("He leído y acepto los:", fontSize = 13.sp, color = Color.Gray)
                            Row(modifier = Modifier.wrapContentWidth()) {
                                Text("Términos", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.clickable { onNavigateTyC() })
                                Text(" y ", fontSize = 13.sp, color = Color.Gray)
                                Text("Privacidad", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.clickable { onNavigatePrivacidad() })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .testTag("btn_register")
                            .background(brush = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(0.8f))), shape = MaterialTheme.shapes.extraLarge)
                            .fillMaxWidth()
                            .height(62.dp),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        enabled = registerState !is UiState.Loading
                    ) {
                        if (registerState is UiState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.background)
                        } else {
                            Text(text = "Registrarse", color = MaterialTheme.colorScheme.background, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // ... (Resto de tu UI, "o iniciar sesión", botón Google, etc) ...
                    // Solo asegúrate de usar onNavigateLogin() en lugar de navController.navigate(...)
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
                                onNavigateLogin()
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