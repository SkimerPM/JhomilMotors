package com.jhomilmotors.jhomilmotorsfff.data.model.product

// El DTO Principal (Espejo de ProductDetailsResponseDTO)
data class ProductDetailsDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val skuBase: String,
    val precioBase: Double,
    val brandId: Long?,
    val marcaNombre: String?,
    val categoryId: Long?,
    val categoriaNombre: String?,
    val activo: Boolean,
    val atributos: List<AtributoResponse>,
    val variantes: List<VarianteResponse>,
    val imagenes: List<ImagenResponse>,
    val promociones: List<PromocionResponse>
) {
    data class AtributoResponse(
        val id: Long?,
        val nombre: String,
        val codigo: String?,
        val tipo: String?,
        val unidad: String?,
        val valorTexto: String?,
        val valorNumerico: Double?
    )
    data class VarianteResponse(
        val id: Long,
        val sku: String,
        val precio: Double,
        val stock: Int,
        val activo: Boolean,
        // La variante tiene sus propios atributos (Color, Talla, etc.)
        val atributos: List<AtributoResponse>,
        val imagenes: List<ImagenResponse>
    )

    data class ImagenResponse(
        val id: Long,
        val url: String,
        val esPrincipal: Boolean,
        val orden: Int
    )

    data class PromocionResponse(
        val nombre: String,
        val tipoDescuento: String,
        val valorDescuento: Double,
        val precioConDescuento: Double
    )
}