package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.AuthResponse
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.UserResponse
import com.jhomilmotors.jhomilmotorsfff.data.repository.AuthRepository
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel : ViewModel(){
    // necesitamos el repository para hacer las llamadas a las apis
    // recordemos que es el intermediario y usaremos sus métodos

    val repository = AuthRepository()

    // ESTADO DE LOS CAMPOS DEL FORMULARIO
    private val _nombre = MutableStateFlow("")
    // solo tiene un getter para lectura, lo que indica que el setter lo controlamos
    //nosotros aqupi en el view model, cumpleiendo con el mvvm al encapsular la variable :>.

    val nombre: StateFlow<String> = _nombre.asStateFlow() // representa lectura para la UI

    private val _apellido = MutableStateFlow("")
    val apellido: StateFlow<String> = _apellido.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // ===== Estado de la operación de registro =====

    //esto que viene se leeria como : definimos un MutableStateFlow de tipo UiState, esa UiState es de tipo UserResponse
    //y su estado inicial del MutableStateFlow es UiState.Idle, que representa el estado inicial en el que no ha ocurrido
    //nada
    private val _registerState = MutableStateFlow<UiState<UserResponse>>(UiState.Idle)

    // se marca que devuelve UserResponse porque dentro del UiState, cuando el estado sea Succes,
    // devolvemos data de algun tipo de dato, y en un registro, lo que devolvemos es el UserResponse. :)
    val registerState: StateFlow<UiState<UserResponse>> = _registerState.asStateFlow()

    // ===== Estado de la operación de login =====

    private val _loginState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle)

    val loginState : StateFlow<UiState<AuthResponse>> = _loginState.asStateFlow()

    // ===== Funciones para actualizar los campos =====

    fun onNombreChange(newValue : String){
        _nombre.value = newValue
    }

    fun onApellidoChange(newValue: String) {
        _apellido.value = newValue
    }

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }
    // Validación básica
    fun registerUser(context: Context){
        if (_nombre.value.isBlank() || _apellido.value.isBlank() ||
            _email.value.isBlank() || _password.value.isBlank()) {
            _registerState.value = UiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            try {
                val response = repository.registerUser(
                    context,
                    nombre = _nombre.value,
                    apellido = _apellido.value,
                    email = _email.value,
                    password= _password.value
                )
                if(response.isSuccessful && response.body() != null){
                    //201 - Creado
                    _registerState.value = UiState.Success(response.body()!!)
                }else{
                    // 4xx o 5xx - Error del servidor
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when {
                        errorBody.isNullOrBlank() -> "Error desconocido (código ${response.code()})"
                        errorBody.trim().startsWith("{") -> {
                            try {
                                val json = JSONObject(errorBody)
                                when {
                                    json.has("message") -> json.getString("message")
                                    json.has("email") -> json.getString("email")
                                    else -> json.toString()
                                }
                            } catch (e: Exception) {
                                "Error desconocido (código ${response.code()})"
                            }
                        }
                        else -> errorBody // es texto plano
                    }
                    _registerState.value = UiState.Error(errorMessage)
                }
            }catch (e: Exception) {
                // Error de red (sin internet, timeout, etc.)
                _registerState.value = UiState.Error(
                    e.message ?: "Error de conexión"
                )
            }
        }
    }

    fun loginUser(context: Context) {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _loginState.value = UiState.Error("Todos los campos son obligatorios")
            return
        }
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            try {
                val response = repository.loginUser(context, _email.value, _password.value)
                if (response.isSuccessful && response.body() != null) {
                    val auth = response.body()!!
                    // GUARDA LOS TOKENS AL HACER LOGIN:
                    TokenManager.saveTokens(context, auth.accessToken, auth.refreshToken)
                    _loginState.value = UiState.Success(auth)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val json = JSONObject(errorBody ?: "{}")
                        json.optString("message",
                            json.optString("email","Credenciales inválidas"))
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