package com.jhomilmotors.jhomilmotorsfff.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmartActionButton(
    modifier: Modifier = Modifier,
    variantId: Long?,
    productId: Long,
    onAddToCart: (Long) -> Unit, // Devuelve el ID de variante
    onViewDetails: (Long) -> Unit // Devuelve el ID de producto
) {
    val isDirectBuy = variantId != null

    FilledIconButton(
        onClick = {
            if (isDirectBuy) {
                // Si hay variante, añadimos al carrito directo
                onAddToCart(variantId!!)
            } else {
                // Si no, mandamos al usuario a elegir
                onViewDetails(productId)
            }
        },
        modifier = modifier.size(32.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            // Diferenciamos visualmente: Primario para compra, Secundario para ver
            containerColor = if (isDirectBuy) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = if (isDirectBuy) Icons.Default.Add else Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = if (isDirectBuy) "Añadir al carrito" else "Ver opciones",
            modifier = Modifier.size(16.dp)
        )
    }
}