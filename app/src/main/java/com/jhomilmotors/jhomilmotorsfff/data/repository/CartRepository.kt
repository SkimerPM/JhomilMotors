package com.jhomilmotors.jhomilmotorsfff.data.repository

import com.jhomilmotors.jhomilmotorsfff.data.model.cart.*
import com.jhomilmotors.jhomilmotorsfff.data.remote.ApiService

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val api: ApiService
    // Si manejas usuarios anónimos, aquí inyectarías tus SharedPreferences para el sessionId
) {
    // Si tienes lógica de usuario anónimo, recupera el ID aquí. Si solo usas login, deja esto null.
    private val currentSessionId: String? = null

    suspend fun getCart(): Response<CartDTO> {
        return api.getCart(currentSessionId)
    }

    suspend fun addItem(variantId: Long, quantity: Int): Response<CartDTO> {
        val request = CreateCartItemRequestDTO(variantId, quantity)
        return api.addItem(request, currentSessionId)
    }

    suspend fun updateItem(itemId: Long, quantity: Int): Response<CartDTO> {
        val request = UpdateCartItemRequestDTO(quantity)
        return api.updateItem(itemId, request, currentSessionId)
    }

    suspend fun removeItem(itemId: Long): Response<CartDTO> {
        return api.removeItem(itemId, currentSessionId)
    }

    suspend fun applyCoupon(code: String): Response<CartDTO> {
        return api.applyCoupon(CouponRequestDTO(code), currentSessionId)
    }

    suspend fun removeCoupon(): Response<CartDTO> {
        return api.removeCoupon(currentSessionId)
    }
}