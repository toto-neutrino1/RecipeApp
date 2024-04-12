package com.example.recipeapp.ui.recipes.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.data.DATA_LOADING
import com.example.recipeapp.databinding.FragmentFavoritesBinding
import com.example.recipeapp.ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentFavoritesBinding is null!")

    private val viewModel: FavoritesViewModel by viewModels()
    private val recipesListAdapter = RecipesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

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
        viewModel.loadFavorites()
        viewModel.favoritesUiState.observe(viewLifecycleOwner) { favoritesState ->
            if (favoritesState.recipesList == null) {
                Toast.makeText(requireContext(), DATA_LOADING, Toast.LENGTH_SHORT).show()
            } else {
                if (favoritesState.recipesList.isNotEmpty()) {
                    binding.tvFavoritesStub.visibility = View.GONE
                    binding.rvFavorites.visibility = View.VISIBLE

                    recipesListAdapter.dataset = favoritesState.recipesList

                    recipesListAdapter.setOnItemClickListener(
                        object : RecipesListAdapter.OnItemClickListener {
                            override fun onItemClick(recipeId: Int) {
                                openRecipeByRecipeId(recipeId)
                            }
                        }
                    )

                    binding.rvFavorites.adapter = recipesListAdapter
                } else {
                    binding.tvFavoritesStub.visibility = View.VISIBLE
                    binding.rvFavorites.visibility = View.GONE
                }
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val directions =
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(directions)
    }
}