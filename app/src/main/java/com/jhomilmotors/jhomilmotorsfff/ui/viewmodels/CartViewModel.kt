package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.cart.CartDTO
import com.jhomilmotors.jhomilmotorsfff.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow<UiState<CartDTO>>(UiState.Loading)
    val cartState: StateFlow<UiState<CartDTO>> = _cartState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = UiState.Loading
            try {
                val response = cartRepository.getCart()
                if (response.isSuccessful && response.body() != null) {
                    _cartState.value = UiState.Success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error ${response.code()}"
                    _cartState.value = UiState.Error("Error al cargar: $errorMsg")
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun updateItemQuantity(itemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "🔄 Actualizando item $itemId a cantidad $newQuantity")

                // NO ponemos UiState.Loading aquí para evitar parpadeos en la UI
                // El backend devuelve el DTO del carrito YA actualizado
                val response = cartRepository.updateItem(itemId, newQuantity)

                if (response.isSuccessful && response.body() != null) {
                    // ✅ Actualizamos el estado directamente con la respuesta nueva
                    _cartState.value = UiState.Success(response.body()!!)
                    Log.d("CartViewModel", "✅ Carrito actualizado correctamente")
                } else {
                    Log.e("CartViewModel", "❌ Error update: ${response.code()}")
                    // Opcional: Podrías emitir un evento de error temporal (Toast)
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Excepción update: ${e.message}")
            }
        }
    }

    fun removeItem(itemId: Long) {
        viewModelScope.launch {
            try {
                Log.d("CartViewModel", "🗑️ Eliminando item $itemId")

                val response = cartRepository.removeItem(itemId)

                if (response.isSuccessful && response.body() != null) {
                    // ✅ Actualizamos el estado directamente
                    _cartState.value = UiState.Success(response.body()!!)
                    Log.d("CartViewModel", "✅ Item eliminado correctamente")
                } else {
                    Log.e("CartViewModel", "❌ Error delete: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Excepción delete: ${e.message}")
            }
        }
    }
}