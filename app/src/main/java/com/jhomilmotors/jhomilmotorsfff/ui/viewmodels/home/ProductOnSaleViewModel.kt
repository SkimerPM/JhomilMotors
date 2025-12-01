package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.SpringPage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductOnSaleDTO
import com.jhomilmotors.jhomilmotorsfff.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductOnSaleViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    // 1. El Estado que observará la Vista (Activity/Fragment/Compose)
    // El tipo es SpringPage<ProductOnSaleDTO> porque necesitamos la info de paginación
    private val _productsOfferState = MutableStateFlow<UiState<SpringPage<ProductOnSaleDTO>>>(UiState.Idle)
    val productsOfferState: StateFlow<UiState<SpringPage<ProductOnSaleDTO>>> = _productsOfferState.asStateFlow()

    // Inicializamos cargando la página 0
    init {
        loadOffers(page = 0)
    }

    // 2. La función para cargar datos
    fun loadOffers(page: Int) {
        viewModelScope.launch {
            // Emitimos estado de carga
            _productsOfferState.value = UiState.Loading

            try {
                val response = repository.getProductOffer(page)

                if (response.isSuccessful) {
                    // El body() puede ser nulo, lo manejamos
                    response.body()?.let { pageData ->
                        _productsOfferState.value = UiState.Success(pageData)
                    } ?: run {
                        _productsOfferState.value = UiState.Error("Respuesta vacía del servidor")
                    }
                } else {
                    _productsOfferState.value = UiState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // Error de red o parseo
                _productsOfferState.value = UiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}