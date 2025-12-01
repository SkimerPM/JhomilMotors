package com.jhomilmotors.jhomilmotorsfff.data.remote

import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.model.GoogleLoginRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.LoginRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.RegisterRequest
import com.jhomilmotors.jhomilmotorsfff.data.model.SpringPage
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.category.CategoryResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductOnSaleDTO
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    //-.####### Home #######
    @GET("/api/v1/catalog/categories/full")
    suspend fun getCategories() : Response<List<CategoryResponse>>

    @GET("/api/v1/catalog/category-activo/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: Int
    ) : Response<SpringPage<ProductResponse>>

    @GET("/api/v1/catalog/product/{id}")
    suspend fun getProductDetails(
        @Path("id") productId: Long
    ) : Response<ProductDetailsDto>

    @GET("/api/promotion-products/on-sale")
    suspend fun getProductOffer(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ) : Response<SpringPage<ProductOnSaleDTO>>

    // Endpoint para login con Google

    @POST("/api/auth/google")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponse>

    // Dentro de tu interface AuthService
    @POST("api/auth/resend-verification")
    suspend fun resendVerification(@Body body: Map<String, String>): Response<Map<String, String>>
}