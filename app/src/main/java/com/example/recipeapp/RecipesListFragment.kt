package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.data.ARG_CATEGORY_ID
import com.example.recipeapp.data.ARG_CATEGORY_IMAGE_URL
import com.example.recipeapp.data.ARG_CATEGORY_NAME
import com.example.recipeapp.data.ARG_RECIPE_ID
import com.example.recipeapp.data.ARG_RECIPE_IMAGE_URL
import com.example.recipeapp.data.ARG_RECIPE_NAME
import com.example.recipeapp.data.STUB
import com.example.recipeapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentListRecipesBinding is null!")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
        setInitScreenContent()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            categoryName = it.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(ARG_CATEGORY_IMAGE_URL)
        }
    }

    private fun setInitScreenContent() {
        binding.tvTitleListRecipesText.text =
            categoryName ?: context?.getString(R.string.title_burgers)

        with(binding.ivTitleListRecipesImage) {
            try {
                setImageDrawable(
                    Drawable.createFromStream(
                        context?.assets?.open(
                            categoryImageUrl ?: "burger.png"
                        ),
                        null
                    )
                )
            } catch (e: Exception) {
                Log.e(
                    "${context?.getString(com.example.recipeapp.R.string.asset_error)}",
                    "${e.printStackTrace()}"
                )
            }

            contentDescription =
                "${context?.getString(com.example.recipeapp.R.string.cont_descr_iv_list_recipes)}" +
                        "$categoryName"
        }
    }

    private fun initRecycler() {
        val recipesListAdapter =
            RecipesListAdapter(
                dataset = STUB.getRecipesByCategoryId(categoryId ?: 0), this
            )

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )

        binding.rvListRecipes.adapter = recipesListAdapter
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack("RecipeFragment")
        }
    }
}