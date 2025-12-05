package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.order.OrderResponse
import com.jhomilmotors.jhomilmotorsfff.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    // Estado principal que contiene la lista completa
    private val _ordersState = MutableStateFlow<UiState<List<OrderResponse>>>(UiState.Idle)
    val ordersState: StateFlow<UiState<List<OrderResponse>>> = _ordersState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _ordersState.value = UiState.Loading
            try {
                val response = repository.getMyOrders()
                if (response.isSuccessful && response.body() != null) {
                    // Extraemos la lista del objeto Page
                    val orders = response.body()!!.content
                    _ordersState.value = UiState.Success(orders)
                } else {
                    _ordersState.value = UiState.Error("No se pudieron cargar los pedidos")
                }
            } catch (e: Exception) {
                _ordersState.value = UiState.Error(e.message ?: "Error de conexión")
            }
        }
    }
}