package com.example.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.recipeapp.databinding.ActivityMainBinding
import java.lang.IllegalArgumentException

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
                add(R.id.mainContainer, CategoriesListFragment())
            }
        }
    }

    fun onClickCategories(view: View) {
        fragmentManager.commit {
            replace(R.id.mainContainer, CategoriesListFragment())
            setReorderingAllowed(true)
            addToBackStack("Categories fragment")
        }
    }

    fun onClickFavorites(view: View) {
        fragmentManager.commit {
            replace(R.id.mainContainer, FavoritesFragment())
            setReorderingAllowed(true)
            addToBackStack("Favorites fragment")
        }
    }
}