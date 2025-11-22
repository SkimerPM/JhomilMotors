package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductResponse
import com.jhomilmotors.jhomilmotorsfff.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    // Estado: Guardamos una lista de Productos
    private val _products = MutableStateFlow<UiState<List<ProductResponse>>>(UiState.Idle)
    val products: StateFlow<UiState<List<ProductResponse>>> = _products.asStateFlow()

    fun loadProducts(categoryId: Int) {
        viewModelScope.launch {
            _products.value = UiState.Loading

            try {
                // 1. Llamar al repositorio usando el categoryId
                val response = repository.getProductsByCategory(categoryId)

                if (response.isSuccessful) {
                    // OJO AQUÍ: La respuesta es una SpringPage,
                    // pero nosotros queremos la lista que está ADENTRO, en .content
                    val page = response.body()
                    val listaProductos = page?.content ?: emptyList()
                    _products.value = UiState.Success(listaProductos)
                } else {
                    _products.value = UiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _products.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}