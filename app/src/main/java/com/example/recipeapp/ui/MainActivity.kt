package com.example.recipeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.URL_GET_CATEGORIES
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("ActivityMainBinding is null!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.categoriesBtn.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.categoriesListFragment)
        }

        binding.favoritesBtn.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.favoritesFragment)
        }

        val categoriesThread = Thread {
            val categoriesUrl = URL(URL_GET_CATEGORIES)
            val categoriesConnection = categoriesUrl.openConnection() as HttpURLConnection
            categoriesConnection.connect()

            Log.i(
                "getCategoriesThread",
                "Выполняю запрос на потоке: ${Thread.currentThread().name}"
            )

            val jsonCategories = categoriesConnection.inputStream.bufferedReader().readText()
            println(jsonCategories)

            val categories: List<Category> =
                Gson().fromJson(jsonCategories, object : TypeToken<List<Category>>(){}.type)
        }

        categoriesThread.start()
        Log.i(
            "MainThread",
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )
    }
}