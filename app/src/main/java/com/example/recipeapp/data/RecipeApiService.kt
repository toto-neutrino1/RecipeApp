package com.example.recipeapp.data

import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("$RECIPE/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("$RECIPES")
    fun getRecipesByIds(@Query("ids") recipesIds: String): Call<List<Recipe>>

    @GET("$CATEGORY/{id}")
    fun getCategoryById(@Path("id") categoryId: Int): Call<Category>

    @GET("$CATEGORY/{id}/$RECIPES")
    fun getRecipesByCategoryId(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("$CATEGORY")
    fun getCategories(): Call<List<Category>>
}