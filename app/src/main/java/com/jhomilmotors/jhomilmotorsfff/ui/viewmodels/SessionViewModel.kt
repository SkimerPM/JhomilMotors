// ui/viewmodels/SessionViewModel.kt
package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.repository.SessionRepository
import com.jhomilmotors.jhomilmotorsfff.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: SessionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _sessionValid = MutableStateFlow(false)
    val sessionValid: StateFlow<Boolean> = _sessionValid.asStateFlow()

    private val _isChecking = MutableStateFlow(true)
    val isChecking: StateFlow<Boolean> = _isChecking.asStateFlow()

    fun checkSession() {
        viewModelScope.launch {
            try {
                val refreshToken = TokenManager.getRefreshToken(context)

                if (!refreshToken.isNullOrBlank()) {
                    val response = repository.refreshToken(refreshToken)

                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!
                        TokenManager.saveTokens(
                            context,
                            authResponse.accessToken,
                            authResponse.refreshToken
                        )
                        _sessionValid.value = true
                    } else {
                        TokenManager.clear(context)
                        _sessionValid.value = false
                    }
                } else {
                    _sessionValid.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                TokenManager.clear(context)
                _sessionValid.value = false
            } finally {
                _isChecking.value = false
            }
        }
    }
}
