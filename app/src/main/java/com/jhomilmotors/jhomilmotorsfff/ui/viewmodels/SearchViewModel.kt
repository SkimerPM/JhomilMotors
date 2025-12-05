package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.SearchResult
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchState = MutableStateFlow<UiState<List<SearchResult>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<SearchResult>>> = _searchState

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery

        // Cancelamos la búsqueda anterior si el usuario sigue escribiendo
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _searchState.value = UiState.Idle
            return
        }

        // Iniciamos nueva búsqueda con un pequeño retraso (500ms)
        searchJob = viewModelScope.launch {
            delay(500) // Debounce: Espera medio segundo a que deje de escribir
            performSearch(newQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _searchState.value = UiState.Loading
        try {
            val response = repository.search(query)
            if (response.isSuccessful && response.body() != null) {
                val results = response.body()!!
                if (results.isEmpty()) {
                    _searchState.value = UiState.Error("No se encontraron productos")
                } else {
                    _searchState.value = UiState.Success(results)
                }
            } else {
                _searchState.value = UiState.Error("Error en el servidor")
            }
        } catch (e: Exception) {
            _searchState.value = UiState.Error(e.message ?: "Error de conexión")
        }
    }
}