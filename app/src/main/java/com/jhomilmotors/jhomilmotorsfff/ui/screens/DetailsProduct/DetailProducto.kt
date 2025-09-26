package com.jhomilmotors.jhomilmotorsfff.ui.screens.DetailsProduct

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons // <- Base para los iconos
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <- Icono de la flecha de volver
import androidx.compose.material.icons.filled.KeyboardArrowDown // <- Icono de la flecha de bajar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider // Necesario para la línea de separación
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.R // Asumo que esta es la referencia correcta a tus recursos


@Composable
fun DetailsProductoScreen() {
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp)
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: navController.popBackStack() */ }) {
                // CORRECCIÓN: Usar la referencia de Icons importada
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    modifier = Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "", // Asumo que el texto del título está intencionalmente vacío
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center, // CORRECCIÓN: Usar la referencia correcta de TextAlign
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider( // Usar Divider para la línea horizontal
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.2.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.10f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bateria_enerjet), // Reemplaza con tu imagen
                contentDescription = "Batería Enerjet",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "BATERIA ENERJET 11T56 56AH 405CCA TOYOTA 11PLC",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "S/320.00",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Sección de Descripción
        ProductSection(title = "Descripción", content = {
            Text(
                text = "La batería Enerjet 11T56 12V 56Ah 405CCA con 11 placas ha sido específicamente diseñada para satisfacer las necesidades energéticas de autos y camionetas Toyota.",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        })

        // Sección Ficha Técnica
        ProductSection(title = "Ficha Técnica", content = {
            Column {
                Text(text = "Voltaje: 12V", fontSize = 14.sp, color = Color(0xFF666666))
                Text(text = "Amperaje: 56Ah", fontSize = 14.sp, color = Color(0xFF666666))
                Text(text = "CCA: 405", fontSize = 14.sp, color = Color(0xFF666666))
            }
        })

        // Sección Productos Relacionados
        ProductSection(title = "Productos Relacionados", content = {
            // Podrías poner aquí un LazyRow con imágenes de productos
            Text(
                text = "Lista de productos relacionados...",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        })
        Spacer(modifier = Modifier.weight(1f))
    }

// --- Sección inferior (Cantidad y Botón) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Control de cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                TextButton(
                    onClick = { if (quantity > 1) quantity-- },
                    colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.size(40.dp),

                ) {
                    Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold,  color = MaterialTheme.colorScheme.onBackground)

                }
                Text(
                    text = "$quantity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(
                    onClick = { quantity++ },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.size(40.dp)
                ) {
                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            // Botón "Añadir al carrito"
            Button(
                onClick = { /* Lógica para añadir al carrito */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E021A)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Añadir al carrito",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

    }
}

// Componente reutilizable para las secciones de descripción, ficha técnica, etc.
@Composable
fun ProductSection(title: String, content: @Composable (() -> Unit)? = null) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { expanded = !expanded } // Invierte el estado (abierto -> cerrado)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Cerrar" else "Expandir",
                    tint = MaterialTheme.colorScheme.onBackground,
                    // 4. ROTACIÓN: Rota el icono 180 grados si está expandido
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        }
        if (content != null && expanded) {
            content()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Separador para la siguiente sección
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDetailsProductoScreen(){
    JhomilMotorsShopTheme{
        DetailsProductoScreen()
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewDetailsProductoScreenClaro(){
    JhomilMotorsShopTheme{
        DetailsProductoScreen()
    }
}