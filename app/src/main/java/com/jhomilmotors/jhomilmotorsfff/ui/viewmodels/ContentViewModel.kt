package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _contentState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val contentState: StateFlow<UiState<String>> = _contentState

    fun loadContent(codigo: String) {
        viewModelScope.launch {
            _contentState.value = UiState.Loading
            try {
                val response = repository.getContent(codigo)
                if (response.isSuccessful && response.body() != null) {
                    val base64String = response.body()!!.contentBase64

                    // DECODIFICAR BASE64 EN ANDROID
                    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                    val htmlString = String(decodedBytes, Charsets.UTF_8)

                    _contentState.value = UiState.Success(htmlString)
                } else {
                    _contentState.value = UiState.Error("Contenido no encontrado")
                }
            } catch (e: Exception) {
                _contentState.value = UiState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}