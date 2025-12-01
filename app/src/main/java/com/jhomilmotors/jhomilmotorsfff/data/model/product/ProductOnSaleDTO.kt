package com.jhomilmotors.jhomilmotorsfff.data.model.product

data class ProductOnSaleDTO (
    val promotionProductId: Long,
    val productId: Long,
    val variantId: Long?, // Puede ser nulo (oferta de producto base)

    // Datos visuales
    val productName: String,
    val sku: String?,
    val imageUrl: String?,

    // Datos de la Promoción
    val promotionLabel: String?, // Ej: "Descuento Verano"
    val discountType: String?,   // "PORCENTAJE", "MONTO_FIJO"

    // Datos Financieros (Usamos Double para mapeo directo del JSON)
    val originalPrice: Double,
    val discountAmount: Double,
    val finalPrice: Double
)