package com.example.recipeapp.ui.recipes.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_RECIPE_ID
import com.example.recipeapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentRecipeBinding is null!")

    private var recipeId: Int? = null
    private val viewModel: RecipeViewModel by activityViewModels()

    private val ingredientsAdapter: IngredientsAdapter = IngredientsAdapter(listOf(), 1)
    private val methodAdapter: MethodAdapter = MethodAdapter(listOf())

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
        arguments?.let {
            recipeId = it.getInt(ARG_RECIPE_ID)
        }

        viewModel.loadRecipe(recipeId)
    }

    private fun initUI() {
        viewModel.recipeUiState.observe(viewLifecycleOwner) { recipeState ->
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
                    viewModel.onFavoritesClicked()
                    if (recipeState.isInFavorites) {
                        setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_heart, null)
                        )
                    } else {
                        setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_heart_empty, null)
                        )
                    }
                }
            }

            with(binding.ivTitleRecipeImage) {
                setImageDrawable(recipeState.recipeImage)
                contentDescription =
                    "${context?.getString(R.string.cont_descr_iv_recipe)} ${recipeState.recipe?.title}"
            }

            initRecyclers(recipeState)
        }
    }

    private fun initRecyclers(recipeUiState: RecipeUiState) {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_item_decoration, null)

        recipeUiState.let {
            with(ingredientsAdapter) {
                dataset = it.recipe?.ingredients ?: listOf()
                quantity = it.recipe?.numOfPortions ?: 1
            }

            methodAdapter.dataset = it.recipe?.method ?: listOf()

            with(binding) {
                sbPortionsQuantity.setOnSeekBarChangeListener(
                    PortionSeekBarListener { progress ->
                        ingredientsAdapter.updateIngredients(progress)
                        viewModel.updateNumOfPortions(progress)
                        tvPortionsQuantity.text = "${it.recipe?.numOfPortions ?: 1}"
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