package com.example.recipeapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipesDatabase
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import kotlinx.coroutines.launch

data class CategoriesUiState(
    val categoriesList: List<Category>? = listOf(),
    val isLoading: Boolean = true
)

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private var _categoriesUiState: MutableLiveData<CategoriesUiState> =
        MutableLiveData(CategoriesUiState())
    val categoriesUiState: LiveData<CategoriesUiState> = _categoriesUiState

    private val recipesRepository: RecipesRepository

    init {
        val db = RecipesDatabase.getDatabase(application)
        recipesRepository = RecipesRepository(db.categoriesDao())
    }

    fun loadCategories() {
        getCategoriesFromCash(isLoading = true)
        viewModelScope.launch {
            val categories = recipesRepository.getCategories()
            recipesRepository.addCategories(categories)
        }

        getCategoriesFromCash(isLoading = false)
    }

    private fun getCategoriesFromCash(isLoading: Boolean) {
        viewModelScope.launch {
            _categoriesUiState.value = _categoriesUiState.value?.copy(
                categoriesList = recipesRepository.getCategoriesFromCash(),
                isLoading = isLoading
            )
        }
    }
}