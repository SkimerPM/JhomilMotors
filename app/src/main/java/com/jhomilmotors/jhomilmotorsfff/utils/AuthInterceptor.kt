package com.jhomilmotors.jhomilmotorsfff.utils

import android.content.Context
import android.util.Log
import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class AuthInterceptor(
    private val appContext: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = TokenManager.getAccessToken(appContext)
        val isLoginEndpoint = request.url.encodedPath.endsWith("/login")
        val isRegisterEndpoint = request.url.encodedPath.endsWith("/register")
        val isRefreshEndpoint = request.url.encodedPath.endsWith("/refresh")

        // Agregar Authorization header si hay token y no es endpoint de auth
        if (!isLoginEndpoint && !isRegisterEndpoint && !isRefreshEndpoint && !accessToken.isNullOrBlank()) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("X-Client-Type", "mobile")
                .build()
        } else if (!isLoginEndpoint && !isRegisterEndpoint && !isRefreshEndpoint) {
            request = request.newBuilder()
                .addHeader("X-Client-Type", "mobile")
                .build()
        }

        var response = chain.proceed(request)

        // Si recibimos 401 y no es en endpoints de auth, intentar refrescar token
        if (response.code == 401 && !isLoginEndpoint && !isRegisterEndpoint && !isRefreshEndpoint) {
            response.close()
            val refreshed = runBlocking { tryRefreshToken(appContext) }

            if (refreshed) {
                val newAccessToken = TokenManager.getAccessToken(appContext)
                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                response = chain.proceed(newRequest)
            }
        }

        return response
    }

    private suspend fun tryRefreshToken(context: Context): Boolean {
        val refreshToken = TokenManager.getRefreshToken(context)
        if (refreshToken.isNullOrBlank()) {
            Log.d("AuthInterceptor", "No refresh token available")
            return false
        }

        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(RefreshApi::class.java)
            val response = api.refreshToken(
                clientType = "mobile",
                refreshRequest = RefreshRequest(refreshRequest = refreshToken)
            )

            if (response.isSuccessful && response.body() != null) {
                val newAuth = response.body()!!
                TokenManager.saveTokens(context, newAuth.accessToken, newAuth.refreshToken)
                Log.d("AuthInterceptor", "Token refreshed successfully")
                true
            } else {
                Log.e("AuthInterceptor", "Refresh failed: ${response.code()}")
                TokenManager.clear(context)
                false
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "Refresh error: ${e.message}")
            TokenManager.clear(context)
            false
        }
    }

    interface RefreshApi {
        @POST("api/auth/refresh")
        suspend fun refreshToken(
            @Header("X-Client-Type") clientType: String = "mobile",
            @Body refreshRequest: RefreshRequest
        ): retrofit2.Response<AuthResponse>
    }
}
