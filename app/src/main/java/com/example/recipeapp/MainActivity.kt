package com.example.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.screens.categoriesList.CategoriesListFragment
import com.example.recipeapp.screens.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("ActivityMainBinding is null!")

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            fragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.mainContainer)
            }
        }

        binding.categoriesBtn.setOnClickListener {
            fragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("Categories fragment")
                replace<CategoriesListFragment>(R.id.mainContainer)
            }
        }

        binding.favoritesBtn.setOnClickListener {
            fragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("Favorites fragment")
                replace<FavoritesFragment>(R.id.mainContainer)
            }
        }
    }
}