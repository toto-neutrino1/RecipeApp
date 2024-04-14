package com.example.recipeapp.data

import android.util.Log
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipesRepository {
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("$BASE_URL/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? {
        var categories: List<Category>? = null

        withContext(Dispatchers.IO) {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()

                categories =
                    categoriesResponse.body()?.map {
                        it.copy(imageUrl = "$IMAGES_URL/${it.imageUrl}")
                    }
            } catch (e: Exception) {
                Log.i("network, getCategories()", "${e.printStackTrace()}")
            }
        }

        return categories
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        var recipes: List<Recipe>? = null

        withContext(Dispatchers.IO) {
            try {
                val recipesCall: Call<List<Recipe>> = service.getRecipesByCategoryId(categoryId)
                val recipesResponse: Response<List<Recipe>> = recipesCall.execute()

                recipes =
                    recipesResponse.body()
                        ?.map { it.copy(imageUrl = "$IMAGES_URL/${it.imageUrl}") }
            } catch (e: Exception) {
                Log.i("network, getRecipesByCategoryId()", "${e.printStackTrace()}")
            }
        }

        return recipes
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        var recipe: Recipe? = null

        withContext(Dispatchers.IO) {
            try {
                val recipeCall: Call<Recipe> = service.getRecipeById(recipeId)
                val recipeResponse: Response<Recipe> = recipeCall.execute()

                recipeResponse.body()?.let {
                    recipe = it.copy(
                        imageUrl = "$IMAGES_URL/${it.imageUrl}",
                        numOfPortions = 1
                    )
                }
            } catch (e: Exception) {
                Log.i("network, getRecipeById()", "${e.printStackTrace()}")
            }
        }

        return recipe
    }

    suspend fun getRecipesByIds(idsSet: Set<Int>, categoryId: Int = 0): List<Recipe>? {
        if (idsSet.isEmpty()) {
            return listOf()
        } else {
            var recipes: List<Recipe>? = null

            withContext(Dispatchers.IO) {
                try {
                    val idsString = idsSet.joinToString(separator = ",")

                    val recipesCall: Call<List<Recipe>> = service.getRecipesByIds(idsString)
                    val recipesResponse: Response<List<Recipe>> = recipesCall.execute()
                    recipes =
                        recipesResponse.body()
                            ?.map { it.copy(imageUrl = "$IMAGES_URL/${it.imageUrl}") }
                } catch (e: Exception) {
                    Log.i("network, getRecipesByIds()", "${e.printStackTrace()}")
                }
            }

            return recipes
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? {
        var category: Category? = null

        withContext(Dispatchers.IO) {
            try {
                val categoryCall: Call<Category> = service.getCategoryById(categoryId)
                val categoryResponse: Response<Category> = categoryCall.execute()

                categoryResponse.body()?.let {
                    category = it.copy(imageUrl = "$IMAGES_URL/${it.imageUrl}")
                }
            } catch (e: Exception) {
                Log.i("network, getCategoryById()", "${e.printStackTrace()}")
            }
        }

        return category
    }
}