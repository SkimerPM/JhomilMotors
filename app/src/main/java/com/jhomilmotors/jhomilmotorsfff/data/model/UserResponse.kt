package com.jhomilmotors.jhomilmotorsfff.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserResponse (
    val id: String,
    val rol: RolResponse,
    val nombre: String,
    val apellido: String,
    val email: String,
    val passwordHash: String,
    val telefono : String?,
    val documento : String?,
    val metodoRegistro: String,
    val googleId: String?,
    val fotoPerfil: String?,
    val activo: Boolean,
    val fechaRegistro: String, //Spring Boot serializa LocalDateTime como ISO 8601 por defecto
    val ultimoAcceso: String?,
)

data class RolResponse(
    val id : String,
    val nombre: String,
    val descripcion: String
)