package com.jhomilmotors.jhomilmotorsfff.ui.screens.profile

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.screens.confirmacion.GradientButton
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

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
            // 2. USAMOS 'ALPHA' PARA CAMBIAR LA APARIENCIA SI ESTÁ DESHABILITADO
            .alpha(if (enabled) 1f else 0.5f)
            .background(gradient, shape = MaterialTheme.shapes.medium)
            // 3. USAMOS 'ENABLED' EN EL MODIFICADOR CLICKABLE
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

//micro composable
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
            enabled = enabled, // El estado del TextField depende de este parámetro
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                // Color del fondo cuando el campo está desenfocado (el estado normal en tu "modo vista")
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                // Color del fondo cuando el campo está enfocado (cuando el usuario está escribiendo)
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                // Color para los campos que nunca se pueden editar (como el de correo)
                disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        )
    }
}

//principal
@Composable
fun ProfileScreen(navController: NavController) {
    // --- ESTADO DE LA UI ---
    // Esta variable controla si estamos en "Modo Vista" o "Modo Edición"
    var isEditMode by remember { mutableStateOf(false) }

    // Variables de estado para cada campo del formulario
    // En el futuro, estos valores vendrán del ViewModel
    var name by remember { mutableStateOf("Omar Enrique") }
    var lastName by remember { mutableStateOf("Castañeda Deza xd") }
    val email = "omar.c@gmail.com" // El email no se edita, así que no necesita estado
    var phone by remember { mutableStateOf("912 345 678") }
    var address by remember { mutableStateOf("Av. Nicolás de Piérola N° 345") }

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
            Spacer(modifier = Modifier.width(48.dp)) // Espacio para centrar el título
        }

        // --- Contenido Principal de la Pantalla ---
        val primaryGradientBrush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF20376D), Color(0xFF3E6AD3))
        )
        val errorGradientBrush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF7F1736), Color(0xFFCB1332))
        )
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
                    onClick = { isEditMode = !isEditMode } // Cambia el estado de edición
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit), // Necesitarás este ícono
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
                onValueChange = { name = it },
                enabled = isEditMode // Se activa/desactiva con el estado
            )
            ProfileTextField(
                label = "Apellidos",
                value = lastName,
                onValueChange = { lastName = it },
                enabled = isEditMode
            )
            ProfileTextField(
                label = "Correo electrónico",
                value = email,
                onValueChange = {},
                enabled = false // El correo nunca es editable
            )
            ProfileTextField(
                label = "Teléfono",
                value = phone,
                onValueChange = { phone = it },
                enabled = isEditMode
            )
            ProfileTextField(
                label = "Dirección",
                value = address,
                onValueChange = { address = it },
                enabled = isEditMode
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones de Acción ---
            GradientButton(
                text = "Guardar Cambios",
                gradient = primaryGradientBrush,
                enabled = isEditMode, // La lógica 'enabled' ahora funciona
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                onClick = {
                    isEditMode = false // Vuelve al modo vista después de guardar
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            GradientButton(
                text = "Cerrar Sesión",
                gradient = errorGradientBrush, // Usamos el nuevo degradado de error
                enabled = true, // Este botón siempre está activo
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                onClick = { /* TODO: Lógica para cerrar sesión */ }
            )
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