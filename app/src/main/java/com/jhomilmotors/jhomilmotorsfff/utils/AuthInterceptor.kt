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
        val path = request.url.encodedPath

        // Detectar endpoints especiales de Auth para no interceptarlos
        val isAuthEndpoint = path.contains("/login") ||
                path.contains("/register") ||
                path.contains("/refresh")

        // 1. Agregar el Token a la petición original si existe
        if (!isAuthEndpoint && !accessToken.isNullOrBlank()) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("X-Client-Type", "mobile")
                .build()
        } else if (!isAuthEndpoint) {
            request = request.newBuilder()
                .addHeader("X-Client-Type", "mobile")
                .build()
        }

        var response = chain.proceed(request)

        // 2. MANEJO DE ERROR 401 (Token Vencido)
        // CORRECCIÓN: Quitamos "&& method == 'GET'" para que funcione en POST, PUT, DELETE también.
        if (response.code == 401 && !isAuthEndpoint) {

            Log.w("AuthInterceptor", "⚠️ 401 detectado en ${request.method} $path. Intentando renovar token...")

            // Cerramos la respuesta anterior para liberar recursos
            response.close()

            // Intentamos renovar el token (Sincronizado)
            val refreshed = runBlocking { tryRefreshToken(appContext) }

            if (refreshed) {
                val newAccessToken = TokenManager.getAccessToken(appContext)

                if (newAccessToken != null) {
                    Log.i("AuthInterceptor", "🔄 Reintentando petición original con NUEVO token...")

                    // Creamos una nueva petición idéntica pero con el token nuevo
                    val newRequest = request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $newAccessToken")
                        .build()

                    // Reintentamos la llamada automáticamente
                    response = chain.proceed(newRequest)
                }
            }
        }

        return response
    }

    private suspend fun tryRefreshToken(context: Context): Boolean {
        val refreshToken = TokenManager.getRefreshToken(context)
        if (refreshToken.isNullOrBlank()) {
            Log.e("AuthInterceptor", "❌ No hay refresh token disponible. Cerrando sesión.")
            TokenManager.clear(context)
            return false
        }

        return try {
            // Creamos un retrofit temporal independiente para evitar ciclos de inyección
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // Asegúrate que esta URL coincida con AppModule
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
                Log.d("AuthInterceptor", "✅ Token renovado exitosamente.")
                true
            } else {
                Log.e("AuthInterceptor", "❌ Falló la renovación del token: ${response.code()}")
                TokenManager.clear(context)
                false
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "❌ Error crítico al renovar token: ${e.message}")
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