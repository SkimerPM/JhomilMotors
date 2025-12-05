package com.jhomilmotors.jhomilmotorsfff.data.model.order

data class OrderResponse(
    val id: Long,
    val fechaPedido: String,
    val total: Double,
    val estado: String,
    val items: List<OrderItemResponse> = emptyList()
)

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String?
)