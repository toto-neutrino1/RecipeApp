package com.example.recipeapp.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_CATEGORY_ID
import com.example.recipeapp.databinding.FragmentListCategoriesBinding
import com.example.recipeapp.ui.recipes.recipesList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("CategoriesListFragmentBinding is null!")

    private val viewModel: CategoriesViewModel by viewModels()
    private val categoriesListAdapter: CategoriesListAdapter = CategoriesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        viewModel.loadCategories()

        viewModel.categoriesUiState.observe(viewLifecycleOwner) { categoriesState ->
            categoriesListAdapter.dataset = categoriesState.categoriesList
            categoriesListAdapter.setOnItemClickListener(
                object : CategoriesListAdapter.OnItemClickListener {
                    override fun onItemClick(categoryId: Int) {
                        openRecipesByCategoryId(categoryId)
                    }
                }
            )

            binding.rvCategories.adapter = categoriesListAdapter
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val bundle = bundleOf(ARG_CATEGORY_ID to categoryId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack("Recipes fragment")
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
        }
    }
}