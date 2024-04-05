package com.example.recipeapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category

data class CategoriesUiState(
    val categoriesList: List<Category>? = listOf()
)

class CategoriesViewModel : ViewModel() {
    private var _categoriesUiState: MutableLiveData<CategoriesUiState> =
        MutableLiveData(CategoriesUiState())
    val categoriesUiState: LiveData<CategoriesUiState> = _categoriesUiState

    private val recipesRepository = RecipesRepository()

    fun loadCategories() {
        _categoriesUiState.value =
            _categoriesUiState.value?.copy(categoriesList = recipesRepository.getCategories())
    }
}