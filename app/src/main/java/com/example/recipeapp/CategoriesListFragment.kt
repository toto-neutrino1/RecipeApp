package com.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("CategoriesListFragmentBinding is null!")

    val categoriesList =
        listOf(
            Category(
                id = 0,
                title = "Бургеры",
                description = "Рецепты всех популярных видов бургеров",
                imageUrl = "burger.png"
            ),
            Category(
                id = 1,
                title = "Десерты",
                description = "Самые вкусные рецепты десертов специально для вас",
                imageUrl = "dessert.png"
            ),
            Category(
                id = 2,
                title = "Пицца",
                description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
                imageUrl = "pizza.png"
            ),
            Category(
                id = 3,
                title = "Рыба",
                description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
                imageUrl = "fish.png"
            ),
            Category(
                id = 4,
                title = "Супы",
                description = "От классики до экзотики: мир в одной тарелке",
                imageUrl = "soup.png"
            ),
            Category(
                id = 5,
                title = "Салаты",
                description = "Хрустящий калейдоскоп под соусом вдохновения",
                imageUrl = "salad.png"
            )
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}