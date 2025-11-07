package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.LoginRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RegisterRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import com.jhomilmotors.jhomilmotorsfff.data.remote.RetrofitClient
import retrofit2.Response
import android.content.Context

class AuthRepository {

    suspend fun registerUser(
        context: Context,
        nombre: String,
        apellido:  String,
        email: String,
        password: String
    ) : Response<UserResponse> {
        val api = RetrofitClient.getApiService(context)
        val request = RegisterRequest(
            nombre = nombre,
            apellido = apellido,
            email = email,
            password = password,
        )
        return api.register(request)
    }

    suspend fun loginUser(
        context: Context,
        email: String,
        password: String
    ): Response<AuthResponse> {
        val api = RetrofitClient.getApiService(context)
        val request = LoginRequest(email = email, password = password)
        return api.login(request)
    }
}
