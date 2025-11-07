package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.repository.LogoutRepository
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthLogoutViewModel @Inject constructor(
    private val repository: LogoutRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val logoutState: StateFlow<UiState<Unit>> = _logoutState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading
            try {

                val response = repository.logout()

                if (response.isSuccessful) {
                    Log.d("AuthLogoutViewModel", "✅ Backend avisado")
                } else {
                    Log.w("AuthLogoutViewModel", "⚠️ Error en backend: ${response.code()}")
                }
                TokenManager.clear(context)
                Log.d("AuthLogoutViewModel", "Tokens locales limpiados")

                _logoutState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                Log.e("AuthLogoutViewModel", "Error: ${e.message}")

                // Aunque haya error, limpia tokens
                TokenManager.clear(context)
                _logoutState.value = UiState.Success(Unit)
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = UiState.Idle
    }
}