package com.jhomilmotors.jhomilmotorsfff.data.model

import com.google.gson.annotations.SerializedName

data class RefreshRequest (
    @SerializedName("refreshToken")
    val refreshRequest : String? = null
)
