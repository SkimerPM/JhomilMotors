package com.jhomilmotors.jhomilmotorsfff.ui.screens.checkout


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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.screens.Payment.AditionalCostsSummary
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

@Composable
fun CheckoutScreen() {
    var selectedShippingOption by remember { mutableStateOf(ShippingOption.PICKUP) }



    // Valores de ejemplo para los precios
    val subtotal = 120.50
    val shippingCost = if (selectedShippingOption == ShippingOption.DELIVERY) 15.00 else 0.00
    val total = subtotal + shippingCost
    val Descuento = 1.00
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        Header()
        AddressSection()
        Direction()
        ShippingSection()

        // Llamadas a las opciones de vnvío
        FirstShippingOptions(
            isSelected = selectedShippingOption == ShippingOption.PICKUP,
            onOptionSelected = { selectedShippingOption = ShippingOption.PICKUP }
        )

        SecondShippingOptions(
            isSelected = selectedShippingOption == ShippingOption.DELIVERY,
            onOptionSelected = { selectedShippingOption = ShippingOption.DELIVERY }
        )

        Spacer(modifier = Modifier.width(250.dp))

        CheckoutSummary(subtotal, shippingCost, total, Descuento)

        RealizarOrdenButton(onClick={
            println("Pedido Realizado")
        })
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) ,

        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver",
            modifier = Modifier.padding(start = 16.dp),
            tint = Color.Black
        )
        Text(
            text = "Checkout",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun AddressSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.location_icon),
                    contentDescription = "Localizador icono",
                    tint = Color(0xFF7E021A)

                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Dirección",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.5.sp,
                    color = Color.Black
                )

            }


            // Icono de pin de ubicación
            // Textos de dirección
            // Botón Editar

            EditButton(onClick = {

            })
        }

        }
    }


@Composable
fun EditButton(onClick: () -> Unit) {
    val cornerRadius = 10.dp
    val borderColor = Color.Gray
    val borderWidth = 0.5.dp
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .border(
                BorderStroke(width = borderWidth, color = borderColor),
                shape = RoundedCornerShape(cornerRadius)
            )
            .height(31.dp)
            .padding(horizontal = 5.dp),
            shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color(0xFF0D224B)
        )
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar dirección",
                modifier = Modifier.size(16.dp)

            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Editar",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify
            )
        }
    }
}


@Composable
fun Direction(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Spacer(modifier = Modifier.height(8.dp))

                Text("Flor Suarez (+51 999 999 999)",
                    color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Los Olivos #34 Mz2 Lt4",
                    color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Chepén",
                    color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Text("La Libertad",
                    color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Perú, 1111",
                    color = Color.Black)
                Spacer(modifier = Modifier.height(10.dp))



            }


        }

    }
}



@Composable
fun   ShippingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
            //.height(38.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,

            ) {

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.shipping_icon),
                    contentDescription = "Localizador icono",
                    tint = Color(0xFF0D224B)

                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Opciones de envio",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.5.sp,
                    color = Color.Black
                )

            }


            // Icono de pin de ubicación
            // Textos de dirección
            // Botón Editar


        }

    }
}


@Composable
fun FirstShippingOptions(
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .clickable { onOptionSelected() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            // Cambia el color de fondo si está seleccionado para mejor feedback
            containerColor = if (isSelected) Color(0xFFE3F2FD)   else Color.White
        ),
        border = if (isSelected) BorderStroke(1.dp, Color(0xFF84AEFF) ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
            // horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = "Recojo en tienda",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected)Color(0xFF1D428A)  else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )


            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF3E6AD3)
                )
            )
            //Spacer(Modifier.width(16.dp))

        }
    }
}


@Composable
fun SecondShippingOptions(
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .clickable { onOptionSelected() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        border = if (isSelected) BorderStroke(1.5.dp, Color(0xFFE3F2FD) ) else null
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Envío a domicilio",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) Color(0xFF1D428A)  else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = isSelected,
                onClick = null, // El clic se maneja en la Card
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF3E6AD3)
                )
            )
            //Spacer(Modifier.width(16.dp))

        }
    }
}
enum class ShippingOption {
    PICKUP,
    DELIVERY
}


@Composable
fun CheckoutSummary(subtotal: Double, shippingCost: Double, total: Double, Descuento: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Resumen del Pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            // Subtotal
            PriceRow("Subtotal", subtotal)
            // Costo de envío
            PriceRow("Costo de envío", shippingCost)
            // Línea divisora
//            Spacer(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(Color.LightGray)
//            )
            //cupón aplicado
            PriceRow("Cupón aplicado", Descuento)

            // Total
            PriceRow("TOTAL", total, isTotal = true)
        }
    }
}

// --------------------------------------------------------------------------------------------------

@Composable
fun PriceRow(label: String, amount: Double, isTotal: Boolean = false) {
    val isDiscountRow = label == "Cupón aplicado"
    val hasActiveDiscount = isDiscountRow && amount > 0.0


    val textColor = when {
        hasActiveDiscount -> Color(0xFF7E021A)
        isTotal -> Color(0xFF1D428A)
        else -> Color.Black
    }

    // Formato del texto para el monto
    val amountText = when {
        isDiscountRow && amount > 0.0 -> "-S/ ${"%.2f".format(amount)}" // Formato con guion para descuento
        isDiscountRow && amount == 0.0 -> "S/ ${"%.2f".format(amount)}" // Mostrar S/ 0.00 si no hay descuento
        else -> "S/ ${"%.2f".format(amount)}"                           // Formato normal para otros montos
    }


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
        Text(
            text = amountText,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color =textColor
        )

    }
}

@Composable
fun RealizarOrdenButton(onClick: () -> Unit) {

    val gradientColors = listOf(
        Color(0xFF1D428A),
        Color(0xFF5C7AE4)
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
                text = "Realizar Orden",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}
//@Composable
//fun SelectOption(){
//    val radioOptions = listOf("Recojo en tienda", "Envío a domicilio")
//    val selectOption by remember{ mutableStateOf(radioOptions[0]) }
//}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen()
}