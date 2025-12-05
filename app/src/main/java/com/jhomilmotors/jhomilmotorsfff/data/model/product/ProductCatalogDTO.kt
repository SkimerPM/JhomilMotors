package com.jhomilmotors.jhomilmotorsfff.data.model.product

data class ProductCatalogDTO(
    val id: Long,
    val nombre: String,
    val descripcion: String?, // Puede ser null
    val precioBase: Double,   // Java BigDecimal -> Kotlin Double
    val stockTotal: Long,
    val imagenUrl: String?,   // Puede ser null

    val categoriaId: Long?,
    val categoriaNombre: String?,
    val marcaId: Long?,
    val marcaNombre: String?
)