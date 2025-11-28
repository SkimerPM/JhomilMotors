package com.jhomilmotors.jhomilmotorsfff.data.model

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    // El nombre en @SerializedName debe coincidir con lo que espera tu Backend (Spring Boot)
    @SerializedName("idToken")
    val idToken: String
)