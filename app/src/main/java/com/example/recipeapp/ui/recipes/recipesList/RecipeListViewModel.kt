package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipesDatabase
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

data class RecipeListUiState(
    val category: Category? = null,
    val recipesList: List<Recipe>? = listOf(),
    val recipeListTitleImageURL: String = "",
    val isLoading: Boolean = true
)

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _recipeListUiState: MutableLiveData<RecipeListUiState> =
        MutableLiveData(RecipeListUiState())
    val recipeListUiState: LiveData<RecipeListUiState> = _recipeListUiState

    private val recipesRepository: RecipesRepository

    init {
        val db = RecipesDatabase.getDatabase(application)
        recipesRepository = RecipesRepository(db.categoriesDao())
    }

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val category = recipesRepository.getCategoryById(categoryId)

            _recipeListUiState.value =
                _recipeListUiState.value?.copy(
                    category = category,
                    recipesList = recipesRepository.getRecipesByCategoryId(categoryId),
                    recipeListTitleImageURL = "${category?.imageUrl}",
                    isLoading = false
                )
        }
    }
}