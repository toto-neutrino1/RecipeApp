package com.example.recipeapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Category

data class CategoriesUiState(
    var categoriesList: List<Category> = listOf()
)

class CategoriesViewModel : ViewModel() {
    private var _categoriesUiState: MutableLiveData<CategoriesUiState> =
        MutableLiveData(CategoriesUiState())
    val categoriesUiState: LiveData<CategoriesUiState> = _categoriesUiState

    fun loadCategories() {
        _categoriesUiState.value?.categoriesList = STUB.getCategories()
    }
}