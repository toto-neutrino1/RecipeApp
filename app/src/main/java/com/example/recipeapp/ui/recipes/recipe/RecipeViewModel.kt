package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel

data class RecipeUiState(
    var categoryId: Int = 0,
    var recipeId: Int = 0,
    var numOfPortions: Int = 1,
    var isInFavorites: Boolean = false
)

class RecipeViewModel : ViewModel() {}