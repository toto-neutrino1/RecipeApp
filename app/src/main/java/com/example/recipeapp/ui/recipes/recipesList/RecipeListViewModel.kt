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
    var category: Category? = null,
    var recipesList: List<Recipe> = listOf(),
    var recipeListTitleImage: Drawable? = null
)

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _recipeListUiState: MutableLiveData<RecipeListUiState> =
        MutableLiveData(RecipeListUiState())
    val recipeListUiState: LiveData<RecipeListUiState> = _recipeListUiState

    fun loadRecipesList(categoryId: Int?) {
        _recipeListUiState.value?.let {
            it.category = STUB.getCategories().find { category -> category.id == categoryId }
            it.recipesList = STUB.getRecipesByCategoryId(categoryId ?: 0)

            try {
                val inputStream =
                    application.assets?.open(it.category?.imageUrl ?: "burger.png")
                it.recipeListTitleImage = Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e(
                    "${application.getString(R.string.asset_error)}",
                    "${e.printStackTrace()}"
                )
            }
        }
    }
}