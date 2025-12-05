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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = "http://10.0.2.2:8080/"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context)) // Tu interceptor de token
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

    // ✅ ÚNICO PROVEEDOR DE API SERVICE (Maneja Auth, Productos y Carrito)
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
    @Provides
    @Singleton
    fun provideCartRepository(api: ApiService): CartRepository {
        return CartRepository(api)
    }
}