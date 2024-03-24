package com.example.recipeapp.ui.recipes.recipesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_CATEGORY_ID
import com.example.recipeapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentListRecipesBinding is null!")

    private var categoryId: Int? = null
    private val viewModel: RecipeListViewModel by viewModels()

    private val recipesListAdapter = RecipesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInputBundleData()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initInputBundleData() {
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
        }

        viewModel.loadRecipesList(categoryId)
    }

    private fun initUI() {
        viewModel.recipeListUiState.observe(viewLifecycleOwner) { recipeListState ->
            binding.tvTitleListRecipesText.text = recipeListState.category?.title

            with(binding.ivTitleListRecipesImage) {
                setImageDrawable(recipeListState.recipeListTitleImage)

                contentDescription =
                    "${context?.getString(R.string.cont_descr_iv_list_recipes)} " +
                            "${recipeListState.category?.title}"
            }

            initRecycler(recipeListState)
        }
    }

    private fun initRecycler(recipeListState: RecipeListUiState) {
        recipesListAdapter.dataset = recipeListState.recipesList

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
        val directions =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(directions)
    }
}