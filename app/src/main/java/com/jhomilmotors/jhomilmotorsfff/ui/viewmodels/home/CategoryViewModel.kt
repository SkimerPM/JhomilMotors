package com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.category.CategoryResponse
import com.jhomilmotors.jhomilmotorsfff.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository : CategoryRepository
) : ViewModel()   {

    private val _categories : MutableStateFlow<UiState<List<CategoryResponse>>> = MutableStateFlow(UiState.Idle)
    val categories : StateFlow<UiState<List<CategoryResponse>>> = _categories.asStateFlow()


    fun loadCategories(){
        viewModelScope.launch {
            _categories.value = UiState.Idle
            try {
                _categories.value = UiState.Loading
                val response = repository.getCategories()
                if (response.isSuccessful){
                    val lista = response.body() ?: emptyList()
                    _categories.value = UiState.Success(lista)
                }else{
                    val error = response.message().toString()
                    _categories.value = UiState.Error(error)
                }

            }catch(ex : Exception){
                _categories.value = UiState.Error(ex.toString())
            }
        }
    }

}