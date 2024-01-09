package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapp.data.ARG_RECIPE_ID
import com.example.recipeapp.data.ARG_RECIPE_IMAGE_URL
import com.example.recipeapp.data.ARG_RECIPE_NAME
import com.example.recipeapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentRecipeBinding is null!")

    private var recipeId: Int? = null
    private var recipeName: String? = null
    private var recipeImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundle()
        setInitScreenContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundle() {
        arguments?.let {
            recipeId = it.getInt(ARG_RECIPE_ID)
            recipeName = it.getString(ARG_RECIPE_NAME)
            recipeImageUrl = it.getString(ARG_RECIPE_IMAGE_URL)
        }
    }

    private fun setInitScreenContent() {
        binding.tvTitleRecipeText.text = recipeName

        with(binding.ivTitleRecipeImage) {
            try {
                setImageDrawable(
                    Drawable.createFromStream(
                        context?.assets?.open(recipeImageUrl ?: "burger-avocado.png"),
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
                "${context?.getString(R.string.cont_descr_iv_recipe)} $recipeName"
        }
    }
}