package com.jhomilmotors.jhomilmotorsfff.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jhomilmotors.jhomilmotorsfff.data.model.ChatMessage
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Estado para el texto que se escribe
    var inputText by remember { mutableStateOf("") }
    // Estado para el scroll automático
    val listState = rememberLazyListState()

    // Scroll automático al fondo cuando llega un mensaje nuevo
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Face,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                "JhomilBot",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface // Color principal fuerte
                            )
                            Text(
                                "Asistente Virtual",
                                style = MaterialTheme.typography.bodySmall,
                                // Usamos un color del tema diseñado para subtítulos, no Color.Gray fijo
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                // Un color de fondo de barra ligeramente distinto al fondo principal
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                // Usamos surfaceVariant como fondo general del chat para que las burbujas blancas resalten más en modo claro
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // LISTA DE MENSAJES
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { msg ->
                    MessageBubble(message = msg)
                }

                if (isLoading) {
                    item {
                        Text(
                            text = "JhomilBot está escribiendo...",
                            style = MaterialTheme.typography.labelSmall,
                            // Usamos el color 'outline' del tema, que es un gris medio con buen contraste
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        )
                    }
                }
            }

            // BARRA DE ENTRADA DE TEXTO
            Surface(
                shadowElevation = 12.dp, // Un poco más de sombra para separar
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp // Le da un ligero tinte de color primario
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), // Un poco más de padding
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Escribe tu consulta...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            // Asegura que el fondo del input sea limpio
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = "" // Limpiar campo
                            }
                        },
                        // Usamos primaryContainer para un look más moderno en el FAB pequeño
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp),
                        elevation = FloatingActionButtonDefaults.elevation(2.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.isFromUser

    // Definimos los pares de colores según quién envía
    val bubbleColor = if (isUser) {
        // Usuario: Color Primario (ej: Azul/Naranja intenso)
        MaterialTheme.colorScheme.primary
    } else {
        // Bot: Usamos 'Surface' (Blanco en modo claro, gris oscuro en modo oscuro)
        // Esto hace que resalte sobre el fondo 'surfaceVariant' del chat.
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isUser) {
        // Texto sobre primario (ej: Blanco)
        MaterialTheme.colorScheme.onPrimary
    } else {
        // Texto sobre superficie (ej: Negro/Gris casi negro)
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isUser) 20.dp else 4.dp, // Esquina puntiaguda para el bot
                bottomEnd = if (isUser) 4.dp else 20.dp  // Esquina puntiaguda para el usuario
            ),
            color = bubbleColor,
            shadowElevation = if (!isUser) 2.dp else 0.dp, // Sombra ligera solo para el bot para dar profundidad
            modifier = Modifier
                .widthIn(max = 280.dp) // Ancho máximo de la burbuja
                .padding(vertical = 6.dp, horizontal = 4.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = textColor,
                fontSize = 16.sp,
                lineHeight = 22.sp // Mejor lectura
            )
        }
    }
}