package com.example.recipeapp.screens.recipe

import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentRecipeBinding is null!")

    private var recipe: Recipe? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInputBundleData()
        initUI()
        initRecyclers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initInputBundleData() {
        arguments?.let {
            recipe = when {
                SDK_INT >= TIRAMISU -> it.getParcelable(ARG_RECIPE, Recipe::class.java)
                else -> it.getParcelable(ARG_RECIPE)
            }

            binding.tvTitleRecipeText.text = recipe?.title
        }
    }

    private fun initUI() {
        binding.tvTitleRecipeText.text = recipe?.title

        with(binding.ivTitleRecipeImage) {
            try {
                setImageDrawable(
                    Drawable.createFromStream(
                        context?.assets?.open(
                            recipe?.imageUrl ?: "burger.png"
                        ),
                        null
                    )
                )
            } catch (e: Exception) {
                Log.e(
                    "${context?.getString(R.string.asset_error)}",
                    "${e.printStackTrace()}"
                )
            }

            contentDescription =
                "${context?.getString(R.string.cont_descr_iv_recipe)}" +
                        "${recipe?.title}"
        }
    }

    private fun initRecyclers() {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_item_decoration, null)

        val ingredientsAdapter = IngredientsAdapter(recipe?.ingredients ?: listOf())
        val methodAdapter = MethodAdapter(recipe?.method ?: listOf())

        with(binding) {
            rvIngredients.adapter = ingredientsAdapter
            rvMethod.adapter = methodAdapter
            rvIngredients.addItemDecoration(ItemDecorationDivider(drawable))
            rvMethod.addItemDecoration(ItemDecorationDivider(drawable))
        }
    }
}