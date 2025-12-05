package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductCatalogDTO
import com.jhomilmotors.jhomilmotorsfff.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    // Estado principal: Contiene la lista acumulada de productos
    private val _feedState = MutableStateFlow<UiState<List<ProductCatalogDTO>>>(UiState.Loading)
    val feedState: StateFlow<UiState<List<ProductCatalogDTO>>> = _feedState.asStateFlow()

    // Estado para saber si estamos cargando la "siguiente página" (para mostrar spinner abajo)
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    // Variables de control de paginación
    private var currentPage = 0
    private var isLastPage = false
    private val currentProductList = mutableListOf<ProductCatalogDTO>()

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        // Evitamos llamadas duplicadas o si ya no hay más páginas
        if (_isLoadingMore.value || isLastPage) return

        viewModelScope.launch {
            // Si es la primera página, el estado principal es Loading. Si no, activamos isLoadingMore
            if (currentPage == 0) {
                _feedState.value = UiState.Loading
            } else {
                _isLoadingMore.value = true
            }

            try {
                val response = repository.getCatalogFeed(currentPage)

                if (response.isSuccessful && response.body() != null) {
                    val pageData = response.body()!!
                    val newProducts = pageData.content

                    // Acumulamos los productos nuevos a la lista existente
                    currentProductList.addAll(newProducts)

                    // Actualizamos variables de control
                    isLastPage = pageData.last
                    currentPage++

                    // Emitimos la lista completa actualizada
                    _feedState.value = UiState.Success(currentProductList.toList())
                } else {
                    if (currentPage == 0) {
                        _feedState.value = UiState.Error("Error: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                if (currentPage == 0) {
                    _feedState.value = UiState.Error(e.message ?: "Error desconocido")
                }
            } finally {
                _isLoadingMore.value = false
            }
        }
    }
}