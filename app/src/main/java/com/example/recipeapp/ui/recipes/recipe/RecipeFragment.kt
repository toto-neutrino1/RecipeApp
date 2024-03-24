package com.example.recipeapp.ui.recipes.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentRecipeBinding is null!")

    private val args: RecipeFragmentArgs by navArgs()

    private val viewModel: RecipeViewModel by viewModels()

    private val ingredientsAdapter: IngredientsAdapter = IngredientsAdapter(listOf())
    private val methodAdapter: MethodAdapter = MethodAdapter(listOf())

    private var isNewFragment: Boolean = true
    private var isClickedOnFavorites: Boolean = false

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initInputBundleData() {
        viewModel.loadRecipe(args.recipeId)
    }

    private fun initUI() {
        viewModel.recipeUiState.observe(viewLifecycleOwner) { recipeState ->
            if (isNewFragment) {
                isNewFragment = false
                with(binding) {
                    tvTitleRecipeText.text = recipeState.recipe?.title
                    tvPortionsQuantity.text = "${recipeState.recipe?.numOfPortions ?: 1}"
                    sbPortionsQuantity.setPadding(0, 0, 0, 0)
                    sbPortionsQuantity.progress = recipeState.recipe?.numOfPortions ?: 1
                }

                with(binding.ibRecipeFavoritesBtn) {
                    setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (recipeState.recipe != null && recipeState.isInFavorites) {
                                R.drawable.ic_heart
                            } else R.drawable.ic_heart_empty,
                            null
                        )
                    )

                    setOnClickListener {
                        isClickedOnFavorites = true
                        viewModel.onFavoritesClicked()
                    }
                }

                with(binding.ivTitleRecipeImage) {
                    setImageDrawable(recipeState.recipeImage)
                    contentDescription =
                        "${context?.getString(R.string.cont_descr_iv_recipe)} ${recipeState.recipe?.title}"
                }

                initRecyclers(recipeState)
            } else {
                with(binding) {
                    tvPortionsQuantity.text = "${recipeState.recipe?.numOfPortions ?: 1}"
                    sbPortionsQuantity.progress = recipeState.recipe?.numOfPortions ?: 1
                }

                if (isClickedOnFavorites) {
                    isClickedOnFavorites = false
                    binding.ibRecipeFavoritesBtn.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            if (recipeState.recipe != null && recipeState.isInFavorites) {
                                R.drawable.ic_heart
                            } else R.drawable.ic_heart_empty,
                            null
                        )
                    )
                }

                ingredientsAdapter.notifyUpdateIngredients()
            }
        }
    }

    private fun initRecyclers(recipeUiState: RecipeUiState) {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_item_decoration, null)

        recipeUiState.let {
            ingredientsAdapter.dataset = it.recipe?.ingredients ?: listOf()

            methodAdapter.dataset = it.recipe?.method ?: listOf()

            with(binding) {
                sbPortionsQuantity.setOnSeekBarChangeListener(
                    PortionSeekBarListener { progress ->
                        viewModel.updateIngredientsAndNumOfPortions(progress)
                    }
                )

                rvIngredients.adapter = ingredientsAdapter
                rvMethod.adapter = methodAdapter

                rvIngredients.addItemDecoration(ItemDecorationDivider(drawable))
                rvMethod.addItemDecoration(ItemDecorationDivider(drawable))
            }
        }
    }
}

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}