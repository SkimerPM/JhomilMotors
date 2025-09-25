package com.jhomilmotors.jhomilmotorsfff.ui.screens.confirmacion

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

//gradient button
//si es que queda, poner como componente para todo el proyecto
@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .background(gradient, shape = MaterialTheme.shapes.medium)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}

//micro composables
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun OrderInfoCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp)
    ) {
        InfoRow(label = "N° de Pedido:", value = "#JM-0001")
        InfoRow(label = "Fecha y Hora:", value = "15/09/2025 - 12:35 p.m.")
        InfoRow(label = "Total Pagado:", value = "S/99.20")
        InfoRow(label = "Método de Pago:", value = "Yape")
        InfoRow(label = "Dirección de Recojo:", value = "Tienda Jhomil Motors (Av. América Sur 123, Trujillo)")
    }
}

//fun principal
@Composable
fun ConfirmationScreen(navController: NavController) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF20376D), // Color de inicio (izquierda)
            Color(0xFF3E6AD3)  // Color de fin (derecha)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_check_success),
            contentDescription = "Pedido confirmado",
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF3E6AD3)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Gracias por tu compra!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu pedido ha sido recibido y está siendo procesado",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        OrderInfoCard()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hemos enviado una copia de este comprobante y los detalles de tu pedido a tu correo electrónico.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(48.dp))

        GradientButton(
            text = "Ver estado de mi pedido",
            gradient = gradientBrush,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            onClick = { }
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Seguir comprando",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val annotatedString = buildAnnotatedString {
            // Parte 1: El texto normal, no clicable
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) {
                append("¿Alguna pregunta? ")
            }

            // Parte 2: El texto clicable y con estilo diferente
            // Le ponemos un "tag" para identificarlo en el clic
            pushStringAnnotation(tag = "support_link", annotation = "support_link")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            ) {
                append("Contacta a soporte")
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "support_link", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        // Si el clic fue en el link, ejecuta esta acción
                        // Descomentar y ajustar la ruta cuando la pantalla de soporte exista
                        // navController.navigate("support_screen")
                    }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationScreenPreview() {
    JhomilMotorsShopTheme {
        ConfirmationScreen(navController = rememberNavController())
    }
}