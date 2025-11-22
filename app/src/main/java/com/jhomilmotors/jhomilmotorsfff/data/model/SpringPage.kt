package com.jhomilmotors.jhomilmotorsfff.data.model

import com.google.gson.annotations.SerializedName

// La T será reemplazada por lo que sea que venga en la lista (ej: Productos)
data class SpringPage<T>(
    @SerializedName("content")
    val content: List<T>,

    @SerializedName("last")
    val last: Boolean,         // ¿Es la última página?

    @SerializedName("totalPages")
    val totalPages: Int,       // Total de páginas disponibles

    @SerializedName("totalElements")
    val totalElements: Int
)