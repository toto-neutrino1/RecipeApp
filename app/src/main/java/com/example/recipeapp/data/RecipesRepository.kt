package com.example.recipeapp.data

import android.util.Log
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RecipesRepository {
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("$BASE_URL/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    private val threadPool = Executors.newFixedThreadPool(3)

    fun getCategories(): List<Category>? {
        var categories: List<Category>? = null
        threadPool.execute {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()

                categories = categoriesResponse.body()
            } catch (e: Exception) {
                Log.i("network, getCategories()", "${e.printStackTrace()}")
            }
        }

        var count = 0
        while (categories.isNullOrEmpty() && count < 10) {
            Thread.sleep(1000)
            count++
        }

        return categories
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        var recipes: List<Recipe>? = null
        threadPool.execute {
            try {
                val recipesCall: Call<List<Recipe>> = service.getRecipesByCategoryId(categoryId)
                val recipesResponse: Response<List<Recipe>> = recipesCall.execute()

                recipes = recipesResponse.body()
            } catch (e: Exception) {
                Log.i("network, getRecipesByCategoryId()", "${e.printStackTrace()}")
            }
        }

        var count = 0
        while (recipes.isNullOrEmpty() && count < 10) {
            Thread.sleep(1000)
            count++
        }

        return recipes
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        var recipe: Recipe? = null
        threadPool.execute {
            try {
                val recipeCall: Call<Recipe> = service.getRecipeById(recipeId)
                val recipeResponse: Response<Recipe> = recipeCall.execute()

                recipe = recipeResponse.body()
                recipe?.numOfPortions = 1
            } catch (e: Exception) {
                Log.i("network, getRecipeById()", "${e.printStackTrace()}")
            }
        }

        var count = 0
        while (recipe == null && count < 10) {
            Thread.sleep(1000)
            count++
        }

        return recipe
    }

    fun getRecipesByIds(idsSet: Set<Int>, categoryId: Int = 0): List<Recipe>? {
        var recipes: List<Recipe>? = null
        threadPool.execute {
            try {
                val idsString = idsSet.joinToString(separator = ",")

                val recipesCall: Call<List<Recipe>> = service.getRecipesByIds(idsString)
                val recipesResponse: Response<List<Recipe>> = recipesCall.execute()
                recipes = recipesResponse.body()
            } catch (e: Exception) {
                Log.i("network, getRecipesByIds()", "${e.printStackTrace()}")
            }
        }

        var count = 0
        while (recipes.isNullOrEmpty() && count < 10) {
            Thread.sleep(1000)
            count++
        }

        return recipes
    }

    fun getCategoryById(categoryId: Int): Category? {
        var category: Category? = null
        threadPool.execute {
            try {
                val categoryCall: Call<Category> = service.getCategoryById(categoryId)
                val categoryResponse: Response<Category> = categoryCall.execute()

                category = categoryResponse.body()
            } catch (e: Exception) {
                Log.i("network, getCategoryById()", "${e.printStackTrace()}")
            }
        }

        var count = 0
        while (category == null && count < 10) {
            Thread.sleep(1000)
            count++
        }

        return category
    }
}