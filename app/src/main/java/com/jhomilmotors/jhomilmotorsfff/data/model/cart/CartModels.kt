package com.jhomilmotors.jhomilmotorsfff.data.model.cart

import com.google.gson.annotations.SerializedName

// ==========================================
// 1. LO QUE RECIBES DEL BACKEND (RESPUESTAS)
// ==========================================

data class CartDTO(
    val id: Long,
    val usuarioId: Long?,
    val sessionId: String?,
    val fechaCreacion: String?,      // LocalDateTime viene como String ISO
    val fechaActualizacion: String?,
    val activo: Boolean,

    // Lista de items (Si viene null, usamos lista vacía)
    val items: List<CartItemDTO> = emptyList(),

    // Totales
    val subtotal: Double,
    val descuentoTotal: Double,
    val impuestos: Double,
    val costoEnvio: Double,
    val total: Double,

    // Promociones
    val cuponesAplicados: List<CuponAplicadoDTO> = emptyList()
)

data class CartItemDTO(
    val id: Long,
    val carritoId: Long,

    // Datos de la variante
    val varianteId: Long,
    val varianteSku: String?,
    val productoNombre: String,
    val imagenUrl: String?,

    // Cantidad y precio
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,

    // Stock para validar en la UI
    val stockDisponible: Int
)

data class CuponAplicadoDTO(
    val codigo: String,
    val nombre: String,
    val descuentoAplicado: Double
)

// ==========================================
// 2. LO QUE ENVÍAS AL BACKEND (REQUESTS)
// ==========================================

data class CreateCartItemRequestDTO(
    @SerializedName("varianteId") // Mapea 'variantId' de Kotlin a 'varianteId' de JSON
    val variantId: Long,

    @SerializedName("cantidad")   // Mapea 'quantity' de Kotlin a 'cantidad' de JSON
    val quantity: Int
)

data class UpdateCartItemRequestDTO(
    @SerializedName("cantidad")   // Aseguramos que al actualizar también se envíe en español
    val quantity: Int
)

data class CouponRequestDTO(
    @SerializedName("codigo")     // Aseguramos el nombre exacto
    val codigo: String
)

data class SyncCartRequestDTO(
    @SerializedName("items")
    val items: List<CreateCartItemRequestDTO>
)