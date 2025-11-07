// ui/viewmodels/AuthViewModel.kt
package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import com.jhomilmotors.jhomilmotorsfff.data.repository.AuthRepository
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _apellido = MutableStateFlow("")
    val apellido: StateFlow<String> = _apellido.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<UserResponse>>(UiState.Idle)
    val registerState: StateFlow<UiState<UserResponse>> = _registerState.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<AuthResponse>> = _loginState.asStateFlow()

    fun onNombreChange(newValue: String) { _nombre.value = newValue }
    fun onApellidoChange(newValue: String) { _apellido.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPasswordChange(newValue: String) { _password.value = newValue }

    fun registerUser() {
        if (_nombre.value.isBlank() || _apellido.value.isBlank() ||
            _email.value.isBlank() || _password.value.isBlank()
        ) {
            _registerState.value = UiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            try {
                val response = repository.registerUser(
                    nombre = _nombre.value,
                    apellido = _apellido.value,
                    email = _email.value,
                    password = _password.value
                )
                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = UiState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when {
                        errorBody.isNullOrBlank() -> "Error desconocido (código ${response.code()})"
                        errorBody.trim().startsWith("{") -> {
                            try {
                                val json = JSONObject(errorBody)
                                json.optString("message", json.optString("email", "Error desconocido"))
                            } catch (e: Exception) {
                                "Error desconocido"
                            }
                        }
                        else -> errorBody
                    }
                    _registerState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _registerState.value = UiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun loginUser() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _loginState.value = UiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            try {
                val response = repository.loginUser(_email.value, _password.value)
                if (response.isSuccessful && response.body() != null) {
                    val auth = response.body()!!
                    TokenManager.saveTokens(context, auth.accessToken, auth.refreshToken)
                    _loginState.value = UiState.Success(auth)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val json = JSONObject(errorBody ?: "{}")
                        json.optString("message", json.optString("email", "Credenciales inválidas"))
                    } catch (e: Exception) {
                        errorBody ?: "Credenciales inválidas"
                    }
                    _loginState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = UiState.Idle
    }
}
