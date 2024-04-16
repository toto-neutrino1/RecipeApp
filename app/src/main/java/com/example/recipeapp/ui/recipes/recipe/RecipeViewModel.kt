package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.NUM_OF_INGREDIENT_MANTIS
import com.example.recipeapp.data.RecipesDatabase
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_FILE_NAME
import com.example.recipeapp.data.SHARED_FAVORITES_IDS_KEY
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isInFavorites: Boolean = false,
    val recipeImageURL: String = "",
    val isLoading: Boolean = true
)

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val favoritesIdsStringSet = getFavorites()

    private var _recipeUiState: MutableLiveData<RecipeUiState> = MutableLiveData(RecipeUiState())
    val recipeUiState: LiveData<RecipeUiState> = _recipeUiState

    private val recipesRepository: RecipesRepository

    init {
        val db = RecipesDatabase.getDatabase(application)
        recipesRepository = RecipesRepository(db.categoriesDao())
    }

    fun loadRecipe(recipeId: Int?) {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipeById(recipeId ?: -1)

            recipe?.ingredients?.forEach {
                val quantity = it.quantity.toDoubleOrNull()
                if (quantity != null) {
                    it.quantity =
                        if (isInteger(quantity)) {
                            "${quantity.toInt()}"
                        } else {
                            "%.${NUM_OF_INGREDIENT_MANTIS}f".format(
                                locale = Locale.US,
                                quantity
                            )
                        }
                }
            }

            _recipeUiState.value =
                _recipeUiState.value?.copy(
                    recipe = recipe,
                    isInFavorites = "$recipeId" in favoritesIdsStringSet,
                    recipeImageURL = "${recipe?.imageUrl}",
                    isLoading = false
                )
        }
    }

    fun onFavoritesClicked() {
        val newFavoritesFlag: Boolean
        with(_recipeUiState.value) {
            newFavoritesFlag = if (this?.recipe != null && this.isInFavorites) {
                favoritesIdsStringSet.remove("${this.recipe.id}")
                false
            } else {
                favoritesIdsStringSet.add("${this?.recipe?.id}")
                true
            }
            saveFavorites(favoritesIdsStringSet)
        }

        _recipeUiState.postValue(recipeUiState.value?.copy(isInFavorites = newFavoritesFlag))
    }

    fun updateIngredientsAndNumOfPortions(progress: Int) {
        _recipeUiState.value?.recipe?.let { recipe ->
            recipe.ingredients.forEach {
                val quantity = it.quantity.toDoubleOrNull()
                if (quantity != null) {
                    val componentQuantity = quantity * progress / recipe.numOfPortions

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
            }

            recipe.numOfPortions = progress
        }

        _recipeUiState.postValue(recipeUiState.value)
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