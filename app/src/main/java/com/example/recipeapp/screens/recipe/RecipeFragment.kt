package com.example.recipeapp.screens.recipe

import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("FragmentRecipeBinding is null!")

    private var recipe: Recipe? = null

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
            recipe = when {
                SDK_INT >= TIRAMISU -> it.getParcelable(ARG_RECIPE, Recipe::class.java)
                else -> it.getParcelable(ARG_RECIPE)
            }
        }
    }

    private fun initUI() {
        with(binding) {
            tvTitleRecipeText.text = recipe?.title
            tvPortionsQuantity.text = "${recipe?.numOfPortions ?: 1}"
            sbPortionsQuantity.progress = recipe?.numOfPortions ?: 1
        }

        with(binding.ivTitleRecipeImage) {
            try {
                val inputStream = context?.assets?.open(recipe?.imageUrl ?: "burger.png")
                val drawable = Drawable.createFromStream(inputStream, null)
                setImageDrawable(drawable)
            } catch (e: Exception) {
                Log.e(
                    "${context?.getString(R.string.asset_error)}",
                    "${e.printStackTrace()}"
                )
            }

            contentDescription =
                "${context?.getString(R.string.cont_descr_iv_recipe)} ${recipe?.title}"
        }
    }

    private fun initRecyclers() {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_item_decoration, null)

        val ingredientsAdapter = IngredientsAdapter(
            recipe?.ingredients ?: listOf(), recipe?.numOfPortions ?: 1
        )
        val methodAdapter = MethodAdapter(recipe?.method ?: listOf())

        with(binding) {
            sbPortionsQuantity.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?, progress: Int, fromUser: Boolean
                    ) {
                        ingredientsAdapter.updateIngredients(progress)
                        tvPortionsQuantity.text = "$progress"
                        recipe?.numOfPortions = progress
                        rvIngredients.adapter?.notifyDataSetChanged()
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