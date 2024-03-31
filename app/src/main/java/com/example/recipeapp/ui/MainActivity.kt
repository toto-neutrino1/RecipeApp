package com.example.recipeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.URL_GET_CATEGORIES
import com.example.recipeapp.data.URL_GET_RECIPES_SUFFIX
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("ActivityMainBinding is null!")

    override fun onCreate(savedInstanceState: Bundle?) {
        var categoriesIds: List<Int> = listOf()

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.categoriesBtn.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.categoriesListFragment)
        }

        binding.favoritesBtn.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.favoritesFragment)
        }

        Log.i(
            "MainThread",
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )

        val threadPool = Executors.newFixedThreadPool(10)

        threadPool.execute {
            val categoriesUrl = URL(URL_GET_CATEGORIES)

            val categoriesLogging =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val categoriesClient = OkHttpClient.Builder().addInterceptor(categoriesLogging).build()
            val categoriesRequest = Request.Builder().url(categoriesUrl).build()

            categoriesClient.newCall(categoriesRequest).execute().use { response ->
                Log.i(
                    "getCategoriesThread",
                    "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                )

                val jsonCategories = response.body?.string()
                println(jsonCategories)

                val categories: List<Category> =
                    Gson().fromJson(jsonCategories, object : TypeToken<List<Category>>() {}.type)
                categoriesIds = categories.map { it.id }
            }
        }

        while (categoriesIds.isEmpty()) {
            Thread.sleep(1000)
        }

        categoriesIds.forEach { id ->
            threadPool.execute {
                val recipesUrl = URL("$URL_GET_CATEGORIES/$id/$URL_GET_RECIPES_SUFFIX")

                val recipesLogging =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                val recipesClient = OkHttpClient.Builder().addInterceptor(recipesLogging).build()
                val recipesRequest = Request.Builder().url(recipesUrl).build()

                recipesClient.newCall(recipesRequest).execute().use { response ->
                    Log.i(
                        "getRecipesByIdsThread",
                        "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                    )

                    val jsonRecipes = response.body?.string()
                    val recipes: List<Recipe> =
                        Gson().fromJson(jsonRecipes, object : TypeToken<List<Recipe>>() {}.type)

                    println(recipes)
                }
            }
        }
    }
}