package com.jhomilmotors.jhomilmotorsfff.ui.screens.Payment


import androidx.compose.animation.core.copy
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

@Composable
fun PaymentMethodsScreen() {
    //var selectedShippingOption by remember { mutableStateOf(ShippingOption.PICKUP) }
    var selectedPaymentOption by remember { mutableStateOf("Yape") }
    val totalParaProbar = 99.00

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        Header()


        Spacer(modifier = Modifier.width(250.dp))
        //CheckoutSummary(subtotal, shippingCost, total, Descuento)
        PaymentOptionCard(
            title = "Yape",
            subtitle = "Pagar con la app",
            isSelected = selectedPaymentOption == "Yape",
            onOptionSelected = { selectedPaymentOption = "Yape" },
            iconPainter = painterResource(id = R.drawable.yape_icon)
        )
        PaymentOptionCard(
            title = "Transferencia Bancaria",
            subtitle = "Depósito directo a la cuenta",
            isSelected = selectedPaymentOption == "Transferencia Bancaria",
            onOptionSelected = { selectedPaymentOption = "Transferencia Bancaria" },
            iconPainter = painterResource(id = R.drawable.bank_icon)
        )
        PaymentOptionCard(
            title = "Plin",
            subtitle = "Pagar con la app",
            isSelected = selectedPaymentOption == "Plin",
            onOptionSelected = { selectedPaymentOption = "Plin" },
            iconPainter = painterResource(id = R.drawable.plin_icon)
        )

        AditionalCostsSummary(previousTotal = totalParaProbar)
        RealizarOrdenButton(onClick={
            println("Pedido Realizado")
        })
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp),
            tint = Color.Black,

        )


        Text(
            text = "Métodos de pago",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            color = Color.Black
        )


    }
    Text(
        text = "Elige tu método de pago",
        fontSize = 19.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        textAlign = TextAlign.Center,
        color = Color.Black,

    )
}

@Composable
fun PaymentOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit,
    iconPainter: Painter,
    iconTint: Color? = null
) {

    val borderColor = if (isSelected) Color(0xFF1D428A)
    else  Color.Gray
    val backgroundColor = if (isSelected )Color(0xFFE3F2FD)    else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 2.dp)
            .clickable { onOptionSelected() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(width = 0.5.dp, color = borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = iconPainter, // Reemplaza con tu ícono
                    contentDescription = "Método de pago",
                    modifier = Modifier.size(24.dp),
                    tint = iconTint ?: Color.Unspecified
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(35.dp)
                        .background(Color.LightGray)
                )


                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // El RadioButton
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF167AC9),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}


@Composable
fun AditionalCostsSummary(previousTotal: Double) {
    // costo adicional
    val aditionalCost = 0.20
    val finalTotal = previousTotal + aditionalCost

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp)
            .padding(bottom = 30.dp, top = 30.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Costos adicionales",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "S/ %.2f".format(aditionalCost),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            // Línea entr el icono y el colum title a& subtitle
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL A PAGAR",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "S/ %.2f".format(finalTotal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun RealizarOrdenButton(onClick: () -> Unit) {

    val gradientColors = listOf(
        Color(0xFF1D428A),
        Color(0xFF7E021A)  ,

    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 30.dp, vertical = 2.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
//        elevation = ButtonDefaults.buttonElevation(
//            defaultElevation = 4.dp //
//        )
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(colors = gradientColors),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Confirmar y Pagar",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PaymentMethodsPreview() {
    PaymentMethodsScreen()
}