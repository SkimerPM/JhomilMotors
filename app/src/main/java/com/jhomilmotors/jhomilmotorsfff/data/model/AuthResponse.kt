package com.jhomilmotors.jhomilmotorsfff.data.model
data class AuthResponse(
    val accessToken: String,
    val email: String,
    val role: String,
    val refreshToken : String?,
)