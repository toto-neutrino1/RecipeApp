package com.example.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.TAG_RECIPE_VIEW_MODEL
import com.example.recipeapp.model.Recipe

data class RecipeUiState(
    var recipe: Recipe? = null,
    var numOfPortions: Int = 1,
    var isInFavorites: Boolean = false
)

class RecipeViewModel : ViewModel() {
    private var _recipeUiState: MutableLiveData<RecipeUiState> = MutableLiveData()
    val recipeUiState: LiveData<RecipeUiState> = _recipeUiState

    init {
        Log.i(TAG_RECIPE_VIEW_MODEL, "RecipeViewModel init block")
        _recipeUiState.value = RecipeUiState(isInFavorites = true)
    }
}