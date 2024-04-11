package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe

data class RecipeListUiState(
    val category: Category? = null,
    val recipesList: List<Recipe>? = listOf(),
    val recipeListTitleImageURL: String = ""
)

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _recipeListUiState: MutableLiveData<RecipeListUiState> =
        MutableLiveData(RecipeListUiState())
    val recipeListUiState: LiveData<RecipeListUiState> = _recipeListUiState

    private val recipesRepository = RecipesRepository()

    fun loadRecipesList(categoryId: Int) {
        val category = recipesRepository.getCategoryById(categoryId)

        _recipeListUiState.value =
            _recipeListUiState.value?.copy(
                category = category,
                recipesList = recipesRepository.getRecipesByCategoryId(categoryId),
                recipeListTitleImageURL = "${category?.imageUrl}"
            )
    }
}