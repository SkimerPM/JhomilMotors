package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: CategoryRepository,
    // Capturamos el ID que viene de la navegación:
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Obtenemos el ID del producto que se navegó.
    // Recordatorio: el nombre debe coincidir con el navArgument: "productId"
    private val productId: Long = savedStateHandle.get<Int>("productId")?.toLong()
        ?: throw IllegalArgumentException("Product ID must be provided")

    // Estado: Guardaremos el DTO de detalle completo
    private val _state = MutableStateFlow<UiState<ProductDetailsDto>>(UiState.Idle)
    val state: StateFlow<UiState<ProductDetailsDto>> = _state.asStateFlow()

    init {
        // Al crearse el VM, iniciamos la carga
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            try {
                // Aquí usamos el ID que capturamos en el constructor
                val response = repository.getProductDetails(productId)

                if (response.isSuccessful) {
                    val details = response.body()
                    if (details != null) {
                        _state.value = UiState.Success(details)
                    } else {
                        _state.value = UiState.Error("Detalles no encontrados.")
                    }
                } else {
                    _state.value = UiState.Error("Error al obtener detalles: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = UiState.Error("Fallo de conexión o parseo.")
            }
        }
    }
}