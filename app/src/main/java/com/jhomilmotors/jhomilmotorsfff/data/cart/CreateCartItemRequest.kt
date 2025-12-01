package com.jhomilmotors.jhomilmotorsfff.data.cart

import com.google.gson.annotations.SerializedName

data class CreateCartItemRequest(
    @SerializedName("varianteId")
    val varianteId: Long,
    @SerializedName("cantidad")
    val cantidad: Int = 1
)