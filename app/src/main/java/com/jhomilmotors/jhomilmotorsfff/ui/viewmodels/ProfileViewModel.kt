// ui/viewmodels/ProfileViewModel.kt
package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.CustomerProfile
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    // ===== Estados de carga =====
    private val _profileState = MutableStateFlow<UiState<CustomerProfile>>(UiState.Idle)
    val profileState: StateFlow<UiState<CustomerProfile>> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<CustomerProfile>>(UiState.Idle)
    val updateState: StateFlow<UiState<CustomerProfile>> = _updateState.asStateFlow()

    // ===== Campos editables =====
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastname = MutableStateFlow("")
    val lastname: StateFlow<String> = _lastname.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    // ===== Funciones de cambio =====
    fun onNameChange(newValue: String) { _name.value = newValue }
    fun onLastnameChange(newValue: String) { _lastname.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPhoneNumberChange(newValue: String) { _phoneNumber.value = newValue }
    fun onAddressChange(newValue: String) { _address.value = newValue }

    // ===== Cargar perfil =====
    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            try {
                val response = repository.getProfile()
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()!!
                    _profileState.value = UiState.Success(profile)

                    // Rellena los campos
                    _name.value = profile.name
                    _lastname.value = profile.lastname
                    _email.value = profile.email
                    _phoneNumber.value = profile.phoneNumber ?: ""  // ← Si es null, usa ""
                    _address.value = profile.address ?: ""
                } else {
                    _profileState.value = UiState.Error("Error al cargar perfil (${response.code()})")
                }
            } catch (e: Exception) {
                _profileState.value = UiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    // ===== Guardar cambios =====
    fun saveProfile() {
        if (_name.value.isBlank() || _lastname.value.isBlank()) {
            _updateState.value = UiState.Error("Nombre y apellido son obligatorios")
            return
        }

        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                val updatedProfile = CustomerProfile(
                    name = _name.value,
                    lastname = _lastname.value,
                    email = _email.value,
                    phoneNumber = _phoneNumber.value,
                    address = _address.value
                )
                Log.d("ProfileViewModel", "Enviando: $updatedProfile")
                val response = repository.updateProfile(updatedProfile)

                Log.d("ProfileViewModel", "Response code: ${response.code()}")
                Log.d("ProfileViewModel", "Response body: ${response.body()}")
                Log.d("ProfileViewModel", "Error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful && response.body() != null) {
                    val updated = response.body()!!
                    _updateState.value = UiState.Success(updated)
                    _profileState.value = UiState.Success(updated)
                } else {
                    _updateState.value = UiState.Error("Error al guardar (${response.code()})")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error: ${e.message}", e)


                _updateState.value = UiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Idle
    }
}
