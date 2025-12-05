package com.jhomilmotors.jhomilmotorsfff.di

import android.content.Context
import com.jhomilmotors.jhomilmotorsfff.data.auth.GoogleAuthClient
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService
import com.jhomilmotors.jhomilmotorsfff.data.repository.AuthRepository
import com.jhomilmotors.jhomilmotorsfff.data.repository.CartRepository
import com.jhomilmotors.jhomilmotorsfff.utils.AuthInterceptor
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ✅ BASE URL: Mantenemos la local (10.0.2.2) para que sigas probando en el emulador.
    // Si necesitas usar ngrok, comenta esta línea y descomenta la otra.
    @Provides
    fun provideBaseUrl(): String = "http://10.0.2.2:8080/"
    // fun provideBaseUrl(): String = "https://nonprovincial-campily-casey.ngrok-free.dev/"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    // ✅ OKHTTP CLIENT COMBINADO
    // Incluye tus interceptors Y los timeouts del remoto para mejor conexión.
    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Del remoto
        .readTimeout(60, TimeUnit.SECONDS)    // Del remoto
        .writeTimeout(60, TimeUnit.SECONDS)   // Del remoto
        .addInterceptor(AuthInterceptor(context)) // Tu interceptor corregido
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ✅ ÚNICO PROVEEDOR DE API SERVICE
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideTokenManager(): TokenManager = TokenManager

    @Singleton
    @Provides
    fun provideAuthRepository(api: ApiService): AuthRepository =
        AuthRepository(api)

    @Singleton
    @Provides
    fun provideGoogleAuthClient(@ApplicationContext context: Context): GoogleAuthClient {
        return GoogleAuthClient(context)
    }

    // ✅ PROVEEDOR DE REPOSITORIO DE CARRITO (Mantenido de tu local)
    @Provides
    @Singleton
    fun provideCartRepository(api: ApiService): CartRepository {
        return CartRepository(api)
    }
}