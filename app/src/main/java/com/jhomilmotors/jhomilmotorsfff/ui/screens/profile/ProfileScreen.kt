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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme


// --- LÓGICA DE INTENTS IMPLÍCITOS ---

fun callSupport(context: Context, phoneNumber: String){
    val dialIntent= Intent(Intent.ACTION_DIAL).apply {
        data= Uri.parse("tel:$phoneNumber")
    }
    if (dialIntent.resolveActivity(context.packageManager)!= null){
        ContextCompat.startActivity(context, dialIntent, null)
    }
}

fun contactViaWhatsapp(context: Context, phoneNumber: String, message: String){
    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
    val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
        data= Uri.parse(url)
    }
    if (whatsappIntent.resolveActivity(context.packageManager) != null){
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
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ){
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
        // Ícono de flecha (KeyboardArrowRight)
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

// --- PANTALLA PRINCIPAL ---

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    // Datos de soporte (Números y correos reales de Jhomil Motors)
    val supportPhone = "51922309105"
    val supportEmail = "fatima.rodriguez@tecsup.edu.pe"
    val whatsappMessage = "Hola Jhomil Motors, necesito ayuda con mi pedido."

    var isEditMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Omar Enrique") }
    var lastName by remember { mutableStateOf("Castañeda Deza") }
    val email = "omar.c@gmail.com"
    var phone by remember { mutableStateOf("912 345 678") }
    var address by remember { mutableStateOf("Av. Nicolás de Piérola N° 345") }

    val primaryGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF20376D), Color(0xFF3E6AD3))
    )
    val errorGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF7F1736), Color(0xFFCB1332))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        // --- Barra Superior ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: navController.popBackStack() */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint= MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(44.dp))
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
                .padding(horizontal = 24.dp)
        ) {
            // --- Botón de Editar ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { isEditMode = !isEditMode }
                ) {
                    // Nota: Asumiendo que 'ic_edit' existe en tu drawable
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Editar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (isEditMode) "Cancelar" else "Editar")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campos del Formulario ---
            ProfileTextField(label = "Nombres", value = name, onValueChange = { name = it }, enabled = isEditMode)
            ProfileTextField(label = "Apellidos", value = lastName, onValueChange = { lastName = it }, enabled = isEditMode)
            ProfileTextField(label = "Correo electrónico", value = email, onValueChange = {}, enabled = false)
            ProfileTextField(label = "Teléfono", value = phone, onValueChange = { phone = it }, enabled = isEditMode)
            ProfileTextField(label = "Dirección", value = address, onValueChange = { address = it }, enabled = isEditMode)
            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones de Acción ---
            GradientButton(
                text = "Guardar Cambios",
                gradient = primaryGradientBrush,
                enabled = isEditMode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                onClick = {
                    // TODO: Lógica de guardado de datos al backend
                    isEditMode = false
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            GradientButton(
                text = "Cerrar Sesión",
                gradient = errorGradientBrush,
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                onClick = { /* TODO: Lógica para cerrar sesión */ }
            )

            // --- SECCIÓN SOPORTE Y CONTACTO (Intents) ---
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Soporte y Contacto",
                style = MaterialTheme.typography.bodyMedium, // Usamos headlineSmall para que quepa mejor
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            // 1. Llamada Telefónica (ACTION_DIAL)
            ContactOptionItem(
                icon = Icons.Default.Phone,
                label = "Llamar a Soporte (Línea Fija)",
                onClick = { callSupport(context, supportPhone) }

            )

            // 2. WhatsApp (ACTION_VIEW)
            ContactOptionItem(
                icon = Icons.Default.Send,
                label = "Contactar por WhatsApp",
                onClick = { contactViaWhatsapp(context, supportPhone, whatsappMessage) }
            )

            // 3. Correo Electrónico (ACTION_SENDTO)
            ContactOptionItem(
                icon = Icons.Default.Email,
                label = "Enviar Correo Electrónico",
                onClick = { sendEmailToSupport(context, supportEmail, "Consulta desde la App Móvil") }
            )

            Spacer(modifier = Modifier.height(32.dp))
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