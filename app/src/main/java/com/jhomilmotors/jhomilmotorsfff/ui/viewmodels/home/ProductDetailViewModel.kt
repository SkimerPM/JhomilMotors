package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.data.repository.CartRepository
import com.jhomilmotors.jhomilmotorsfff.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val cartRepository: CartRepository, // ✅ Inyección REAL del repositorio de carrito
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 1. OBTENCIÓN DEL ID DESDE LA NAVEGACIÓN
    private val productId: Long = savedStateHandle.get<Int>("productId")?.toLong()
        ?: throw IllegalArgumentException("Product ID must be provided")

    // 2. ESTADO DE LOS DETALLES DEL PRODUCTO
    private val _state = MutableStateFlow<UiState<ProductDetailsDto>>(UiState.Idle)
    val state: StateFlow<UiState<ProductDetailsDto>> = _state.asStateFlow()

    // 3. ESTADO PARA LA ACCIÓN DE AGREGAR AL CARRITO
    private val _addToCartState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<Boolean>> = _addToCartState.asStateFlow()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                // Usamos categoryRepository (o productRepository según tu estructura)
                val response = categoryRepository.getProductDetails(productId)

                if (response.isSuccessful && response.body() != null) {
                    _state.value = UiState.Success(response.body()!!)
                } else {
                    _state.value = UiState.Error("Detalles no encontrados. Código: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = UiState.Error("Error al cargar detalles: ${e.message}")
            }
        }
    }

    // 4. FUNCIÓN REAL PARA AGREGAR AL CARRITO (Sin simulación)
    fun addToCart(variantId: Long, quantity: Int) {
        viewModelScope.launch {
            _addToCartState.value = UiState.Loading

            try {
                Log.d("ProductDetailViewModel", "📡 Enviando al servidor -> Variante: $variantId, Cantidad: $quantity")

                // --- LLAMADA AL API REAL ---
                val response = cartRepository.addItem(variantId, quantity)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("ProductDetailViewModel", "✅ Éxito! Producto agregado. Código: ${response.code()}")
                    _addToCartState.value = UiState.Success(true)
                } else {
                    // Intentamos leer el error del body si existe
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("ProductDetailViewModel", "❌ Falló el servidor (${response.code()}): $errorBody")

                    _addToCartState.value = UiState.Error("No se pudo agregar: Error ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "❌ Excepción de red: ${e.message}")
                _addToCartState.value = UiState.Error("Error de conexión. Revisa tu internet.")
            }
        }
    }

    // 5. RESETEAR ESTADO (Para limpiar el mensaje de éxito/error de la UI)
    fun resetCartState() {
        _addToCartState.value = UiState.Idle
    }
}