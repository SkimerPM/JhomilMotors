package com.jhomilmotors.jhomilmotorsfff.data.model.product

data class ProductResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precioBase: Double,
    val stockTotal: Int,
    val imagenUrl: String?,
    val categoriaNombre: String?,
    val marcaNombre: String?
)