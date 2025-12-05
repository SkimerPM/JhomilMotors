package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.ChatMessage
import com.jhomilmotors.jhomilmotorsfff.data.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: AiRepository
) : ViewModel() {

    // Lista de mensajes del chat
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Estado de carga (para mostrar "Escribiendo..." o spinner)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Mensaje de bienvenida automático
        addMessage(ChatMessage(text = "¡Hola! Soy JhomilBot 🤖🔧. ¿En qué puedo ayudarte con tu vehículo hoy?", isFromUser = false))
    }

    fun sendMessage(question: String) {
        if (question.isBlank()) return

        // 1. Agregar mensaje del usuario a la lista inmediatamente
        addMessage(ChatMessage(text = question, isFromUser = true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 2. Llamar al Backend
                val response = repository.askToAssistant(question)

                if (response.isSuccessful && response.body() != null) {
                    val aiResponseText = response.body()!!["respuesta"] ?: "No pude procesar eso."
                    addMessage(ChatMessage(text = aiResponseText, isFromUser = false))
                } else {
                    addMessage(ChatMessage(text = "Lo siento, tuve un error de conexión 🔌.", isFromUser = false))
                }
            } catch (e: Exception) {
                addMessage(ChatMessage(text = "Error: ${e.message}", isFromUser = false))
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addMessage(msg: ChatMessage) {
        // Añadimos al final de la lista
        _messages.value = _messages.value + msg
    }
}