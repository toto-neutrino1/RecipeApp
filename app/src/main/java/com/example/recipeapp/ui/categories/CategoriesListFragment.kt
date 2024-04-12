package com.example.recipeapp.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.data.DATA_LOADING
import com.example.recipeapp.databinding.FragmentListCategoriesBinding

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
            if (categoriesState.categoriesList == null) {
                Toast.makeText(requireContext(), DATA_LOADING, Toast.LENGTH_SHORT).show()
            } else {
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
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val directions =
            CategoriesListFragmentDirections
                .actionCategoriesListFragmentToRecipesListFragment(categoryId)
        findNavController().navigate(directions)
    }
}