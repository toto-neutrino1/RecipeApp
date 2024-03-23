package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.R
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe

data class RecipeListUiState(
    val category: Category? = null,
    val recipesList: List<Recipe> = listOf(),
    val recipeListTitleImage: Drawable? = null
)

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _recipeListUiState: MutableLiveData<RecipeListUiState> =
        MutableLiveData(RecipeListUiState())
    val recipeListUiState: LiveData<RecipeListUiState> = _recipeListUiState

    fun loadRecipesList(categoryId: Int?) {
        try {
            val category = STUB.getCategories().find { category -> category.id == categoryId }
            val inputStream = application.assets?.open(category?.imageUrl ?: "burger.png")

            _recipeListUiState.value =
                _recipeListUiState.value?.copy(
                    category = category,
                    recipesList = STUB.getRecipesByCategoryId(categoryId ?: 0),
                    recipeListTitleImage = Drawable.createFromStream(inputStream, null)
                )
        } catch (e: Exception) {
            Log.e(
                "${application.getString(R.string.asset_error)}",
                "${e.printStackTrace()}"
            )
        }
    }
}