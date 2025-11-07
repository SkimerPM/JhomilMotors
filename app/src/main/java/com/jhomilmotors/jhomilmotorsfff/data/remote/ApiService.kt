package com.jhomilmotors.jhomilmotorsfff.data.remote

import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.model.LoginRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RegisterRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    //-.####### Autenticación #######
    @POST("/api/users")
    suspend fun register(
        //qué enviaremos:
        @Body request: RegisterRequest,
    ) : Response<UserResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Header("X-Client-Type") clientType: String = "mobile"
    ) : Response<AuthResponse>

    @POST("/api/auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("/api/auth/refresh")
    suspend fun refreshToken(
        @Header("X-Client-Type") clientType: String = "mobile",
        @Body request: RefreshRequest,
    ) : Response<AuthResponse>

    //-.####### Perfil de Usuario #######

    @GET("/api/users/me")
    suspend fun getCurrentUserProfile(): Response<CustomerProfile>

    @PUT("/api/users/me")
    suspend fun updateUserProfile(@Body profile: CustomerProfile): Response<CustomerProfile>

}