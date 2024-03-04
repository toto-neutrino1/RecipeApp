package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.R
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_FILE_NAME
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_KEY
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe

data class RecipeUiState(
    var recipe: Recipe? = null,
    var numOfPortions: Int = 1,
    var isInFavorites: Boolean = false,
    var recipeImage: Drawable? = null
)

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val favoritesIdsStringSet = getFavorites()

    private var _recipeUiState: MutableLiveData<RecipeUiState> = MutableLiveData(RecipeUiState())
    val recipeUiState: LiveData<RecipeUiState> = _recipeUiState

    fun loadRecipe(recipeId: Int?) {
        // TODO("load from network")
        _recipeUiState.value?.let {
            if (recipeId != null) {
                it.recipe = STUB.getRecipeById(recipeId = recipeId)
                it.isInFavorites = "$recipeId" in favoritesIdsStringSet

                try {
                    val inputStream =
                        application.assets?.open(it.recipe?.imageUrl ?: "burger.png")
                    it.recipeImage = Drawable.createFromStream(inputStream, null)
                } catch (e: Exception) {
                    Log.e(
                        application.getString(R.string.asset_error),
                        "${e.printStackTrace()}"
                    )
                    it.recipeImage = null
                }
            }
        }
    }

    fun onFavoritesClicked() {
        recipeUiState.value?.let {
            if (it.recipe != null && it.isInFavorites) {
                favoritesIdsStringSet.remove("${it.recipe?.id}")
                it.isInFavorites = false
            } else {
                favoritesIdsStringSet.add("${it.recipe?.id}")
                it.isInFavorites = true
            }

            saveFavorites(favoritesIdsStringSet)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = application.getSharedPreferences(
            SHARED_FAVORITES_IDS_FILE_NAME, Context.MODE_PRIVATE
        )
        val setOfFavoritesIds =
            sharedPrefs?.getStringSet(SHARED_FAVORITES_IDS_KEY, setOf()) ?: setOf()

        return HashSet(setOfFavoritesIds)
    }

    private fun saveFavorites(recipeIds: Set<String>) {
        val sharedPrefs = application.getSharedPreferences(
            SHARED_FAVORITES_IDS_FILE_NAME, Context.MODE_PRIVATE
        )
        if (sharedPrefs != null) {
            with(sharedPrefs.edit()) {
                putStringSet(SHARED_FAVORITES_IDS_KEY, recipeIds)
                apply()
            }
        }
    }
}