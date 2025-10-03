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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.R
import android.content.Context
import android.content.Intent


data class Producto(
    val nombre: String,
    val precio: String,
    val descripcion: String,
    val urlCompartir: String
)


@Composable
fun DetailsProductoScreen(producto: Producto) {
    val context = LocalContext.current

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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(44.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.2.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.10f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bateria_enerjet),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = producto.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = producto.precio,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(
                        onClick = {
                            shareProduct(context, producto.nombre, producto.urlCompartir)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir Producto",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            ProductSection(title = "Descripción", content = {
                Text(
                    text = producto.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            })

            ProductSection(title = "Ficha Técnica", content = {
                Column {
                    Text(text = "Voltaje: 12V", fontSize = 14.sp, color = Color(0xFF666666))
                    Text(text = "Amperaje: 56Ah", fontSize = 14.sp, color = Color(0xFF666666))
                    Text(text = "CCA: 405", fontSize = 14.sp, color = Color(0xFF666666))
                }
            })

            ProductSection(title = "Productos Relacionados", content = {
                Text(
                    text = "Lista de productos relacionados...",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            })
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Cerrar" else "Expandir",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        }
        if (content != null && expanded) {
            content()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}


fun shareProduct(context: Context, nombreProducto: String, urlProducto: String) {
    val mensajeACompartir = "¡Mira qué producto encontré en Jhomil Motors! $nombreProducto. Cómpralo aquí: $urlProducto"

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Producto Interesante en Jhomil Motors")
        putExtra(Intent.EXTRA_TEXT, mensajeACompartir)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Compartir producto usando..."))
}


@Composable
fun DemoProduct(): Producto {
    return Producto(
        nombre = "BATERIA ENERJET 11T56 56AH 405CCA TOYOTA 11PLC",
        precio = "S/320.00",
        descripcion = "La batería Enerjet 11T56 12V 56Ah 405CCA con 11 placas ha sido específicamente diseñada para satisfacer las necesidades energéticas de autos y camionetas Toyota.",
        urlCompartir = "https://jhomilmotors.com/bateria/enerjet-11t56"
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDetailsProductoScreen(){
    JhomilMotorsShopTheme{
        DetailsProductoScreen(producto = DemoProduct())
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewDetailsProductoScreenClaro(){
    JhomilMotorsShopTheme{
        DetailsProductoScreen(producto = DemoProduct())
    }
}