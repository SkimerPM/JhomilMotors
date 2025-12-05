package com.jhomilmotors.jhomilmotorsfff.data.model.order

data class OrderResponse(
    val id: Long,
    val codigo: String?, // Ej: "ORD-2025-001"
    val fechaPedido: String, // Backend manda LocalDateTime (String en JSON)
    val total: Double,
    val estado: String, // PENDIENTE, PAGADO, PREPARANDO, ENVIADO, ENTREGADO, CANCELADO
    val items: List<OrderItemResponse> = emptyList()
)

data class OrderItemResponse(
    val productId: Long,
    val productName: String, // Ajusta si tu DTO usa otro nombre
    val quantity: Int,
    val price: Double,
    val imageUrl: String? // El backend debería mandar esto para mostrar la foto
)