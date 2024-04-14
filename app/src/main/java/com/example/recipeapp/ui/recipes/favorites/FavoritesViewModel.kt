package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_FILE_NAME
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_KEY
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val recipesList: List<Recipe>? = listOf(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _favoritesUiState: MutableLiveData<FavoritesUiState> =
        MutableLiveData(FavoritesUiState())
    val favoritesUiState: LiveData<FavoritesUiState> = _favoritesUiState

    private val recipesRepository = RecipesRepository()

    fun loadFavorites() {
        viewModelScope.launch {
            _favoritesUiState.value =
                _favoritesUiState.value?.copy(
                    recipesList = recipesRepository.getRecipesByIds(getFavoritesIds()),
                    isLoading = false
                )
        }
    }

    private fun getFavoritesIds(): Set<Int> {
        val sharedPrefs = application.getSharedPreferences(
            SHARED_FAVORITES_IDS_FILE_NAME, Context.MODE_PRIVATE
        )
        val setOfFavoritesIds =
            sharedPrefs?.getStringSet(SHARED_FAVORITES_IDS_KEY, setOf()) ?: setOf()

        return setOfFavoritesIds.map { it.toInt() }.toSet()
    }
}