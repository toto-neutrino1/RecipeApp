package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.R
import com.example.recipeapp.data.NUM_OF_INGREDIENT_MANTIS
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_FILE_NAME
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_KEY
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

data class RecipeUiState(
    var recipe: Recipe? = null,
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
        _recipeUiState.value?.let {
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

    fun updateIngredientsAndNumOfPortions(progress: Int) {
        _recipeUiState.value?.recipe?.let { recipe ->
            recipe.ingredients.forEach {
                val componentQuantity = it.quantity.toDouble() * progress / recipe.numOfPortions

                it.quantity =
                    if (isInteger(componentQuantity)) {
                        "${componentQuantity.toInt()}"
                    } else {
                        "%.${NUM_OF_INGREDIENT_MANTIS}f".format(
                            locale = Locale.US,
                            componentQuantity
                        )
                    }
            }

            recipe.numOfPortions = progress
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

private fun isInteger(num: Double) =
    convertToNumWithNeededAccuracy(num.toInt().toDouble(), NUM_OF_INGREDIENT_MANTIS) ==
            convertToNumWithNeededAccuracy(num, NUM_OF_INGREDIENT_MANTIS)

private fun convertToNumWithNeededAccuracy(num: Double, accuracy: Int) =
    BigDecimal("$num")
        .setScale(accuracy, RoundingMode.HALF_UP)
        .stripTrailingZeros().toPlainString()