package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.Recipe

data class RecipeUiState(
    var recipe: Recipe? = null,
    var numOfPortions: Int = 1,
    var isInFavorites: Boolean = false
)

class RecipeViewModel : ViewModel() {
    private var _recipeUiState: MutableLiveData<RecipeUiState> = MutableLiveData()
    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState
}