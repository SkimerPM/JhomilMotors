package com.jhomilmotors.jhomilmotorsfff.ui.screens.cart

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

data class CartProduct(
    val id: Int,
    val imageResId: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)

@Composable
fun CartScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val cartItems = remember {
        mutableStateListOf(
            CartProduct(1, R.drawable.motul_oil, "7100 Aceite motor 10W30 1L FS", 53.00, 1),
            CartProduct(2, R.drawable.bateria_enerjet, "Batería Enerjet 11T56 56AH 405...", 320.00, 1)
        )
    }

    val totalItems = cartItems.sumOf { it.quantity }
    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val taxes = 0.00
    val delivery = 0.00
    val totalFinal = subtotal + taxes + delivery

    val onQuantityChange: (Int, Int) -> Unit = { productId, newQuantity ->
        val index = cartItems.indexOfFirst { it.id == productId }
        if (index != -1) {
            val currentItem = cartItems[index]
            cartItems[index] = currentItem.copy(quantity = newQuantity.coerceAtLeast(1))
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 150.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
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


            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.shopping_cart),
                        contentDescription = "Carrito",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mi Carrito",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Total Items: $totalItems",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 33.dp)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                cartItems.forEach { product ->
                    CartItem(
                        product = product,
                        onQuantityIncrease = { onQuantityChange(product.id, product.quantity + 1) },
                        onQuantityDecrease = { onQuantityChange(product.id, product.quantity - 1) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }


        IntegratedCartSummary(
            modifier = Modifier.align(Alignment.BottomCenter),
            taxes = taxes,
            delivery = delivery,
            totalFinal = totalFinal
        )
    }
}


@Composable
fun IntegratedCartSummary(
    modifier: Modifier = Modifier,
    taxes: Double,
    delivery: Double,
    totalFinal: Double
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                bottom = 60.dp
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            SummaryRow(label = "Impuestos", value = taxes)
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "Delivery", value = delivery)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.2.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.10f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "S/${"%.2f".format(totalFinal)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun CartItem(
    product: CartProduct,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = product.imageResId),
            contentDescription = product.name,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                // CALCULO DEL TOTAL DEL ITEM (Precio unitario * Cantidad)
                text = "S/${"%.2f".format(product.price * product.quantity)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .padding(top = 28.dp)
        ) {
            TextButton(
                onClick = onQuantityDecrease,
                enabled = product.quantity > 1,
                modifier = Modifier.size(35.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("—", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.inversePrimary)
            }
            Spacer(
                modifier = Modifier
                    .height(35.dp)
                    .background(Color.LightGray)
            )
            Text(
                text = "${product.quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 7.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(35.dp)
                    .background(Color.LightGray)
            )

            TextButton(
                onClick = onQuantityIncrease,
                modifier = Modifier.size(35.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("+", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.inversePrimary)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "S/${"%.2f".format(value)}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
    }
}


@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewCartScreen() {
    JhomilMotorsShopTheme {

        val navController = rememberNavController()
        CartScreen(navController = navController)
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewCartScreenClaro() {
    JhomilMotorsShopTheme {

        val navController = rememberNavController()
        CartScreen(navController = navController)
    }
}