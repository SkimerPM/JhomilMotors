package com.jhomilmotors.jhomilmotorsfff.data.model

/**
 * Representa los estados de una operación asíncrona.
 * T es el tipo de dato en caso de éxito.
 */

sealed class UiState <out T>{

    data object Idle: UiState<Nothing>()

    data object Loading: UiState<Nothing>()

    // Operación exitosa con datos
    data class Success<T>(val data: T) : UiState<T>()

    data class Error (val message: String ) : UiState<Nothing>()

}


