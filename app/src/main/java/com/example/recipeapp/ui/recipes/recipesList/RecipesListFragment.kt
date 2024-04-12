package com.example.recipeapp.ui.recipes.recipesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.DATA_LOADING
import com.example.recipeapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentListRecipesBinding is null!")

    private val args: RecipesListFragmentArgs by navArgs()

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
        viewModel.loadRecipesList(args.categoryId)
    }

    private fun initUI() {
        viewModel.recipeListUiState.observe(viewLifecycleOwner) { recipeListState ->
            if (recipeListState.category == null || recipeListState.recipesList == null) {
                Toast.makeText(requireContext(), DATA_LOADING, Toast.LENGTH_SHORT).show()
            } else {
                binding.tvTitleListRecipesText.text = recipeListState.category.title

                with(binding.ivTitleListRecipesImage) {
                    Glide.with(context)
                        .load(recipeListState.recipeListTitleImageURL)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(this)

                    contentDescription =
                        "${context?.getString(R.string.cont_descr_iv_list_recipes)} " +
                                recipeListState.category.title
                }

                initRecycler(recipeListState)
            }
        }
    }

    private fun initRecycler(recipeListState: RecipeListUiState) {
        recipesListAdapter.dataset = recipeListState.recipesList ?: listOf()

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