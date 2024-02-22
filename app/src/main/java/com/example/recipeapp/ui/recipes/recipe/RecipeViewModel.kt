package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.Recipe

data class RecipeUiState(
    var recipe: Recipe? = null,
    var numOfPortions: Int = 1,
    var isInFavorites: Boolean = false
)

class RecipeViewModel : ViewModel() {}