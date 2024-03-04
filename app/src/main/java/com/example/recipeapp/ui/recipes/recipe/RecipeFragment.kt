package com.example.recipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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
                try {
                    val inputStream =
                        context?.assets?.open(recipeState.recipe?.imageUrl ?: "burger.png")
                    val drawable = Drawable.createFromStream(inputStream, null)
                    setImageDrawable(drawable)
                } catch (e: Exception) {
                    Log.e(
                        "${context?.getString(R.string.asset_error)}",
                        "${e.printStackTrace()}"
                    )
                }

                contentDescription =
                    "${context?.getString(R.string.cont_descr_iv_recipe)} ${recipeState.recipe?.title}"
            }
        }
    }

    private fun initRecyclers() {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_item_decoration, null)

        viewModel.recipeUiState.value?.let {
            val ingredientsAdapter = IngredientsAdapter(
                it.recipe?.ingredients ?: listOf(), it.numOfPortions ?: 1
            )
            val methodAdapter = MethodAdapter(it.recipe?.method ?: listOf())

            with(binding) {
                sbPortionsQuantity.setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?, progress: Int, fromUser: Boolean
                        ) {
                            ingredientsAdapter.updateIngredients(progress)
                            tvPortionsQuantity.text = "$progress"
                            it.numOfPortions = progress
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
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