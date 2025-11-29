package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.LoginRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RegisterRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import retrofit2.Response
import android.content.Context
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun registerUser(
        nombre: String,
        apellido:  String,
        email: String,
        password: String
    ) : Response<UserResponse> {
        val request = RegisterRequest(
            nombre = nombre,
            apellido = apellido,
            email = email,
            password = password,
        )
        return api.register(request)
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Response<AuthResponse> {
        val request = LoginRequest(email = email, password = password)
        return api.login(request)
    }

    // ✅ NUEVO
    suspend fun loginWithGoogle(idToken: String): Response<AuthResponse> {
        // Asumiendo que creaste GoogleLoginRequest en ApiService o models
        // Si no, crea el data class: data class GoogleLoginRequest(val idToken: String)
        return api.googleLogin(com.jhomilmotors.jhomilmotorsfff.data.model.GoogleLoginRequest(idToken))
    }
}
