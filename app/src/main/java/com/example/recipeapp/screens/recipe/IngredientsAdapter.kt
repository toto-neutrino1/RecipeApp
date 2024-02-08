package com.example.recipeapp.screens.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.data.NUM_OF_INGREDIENT_MANTIS
import com.example.recipeapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

class IngredientsAdapter(
    private val dataset: List<Ingredient>,
    private var quantity: Int = 1
) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    class IngredientsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIngredientNameText: TextView = view.findViewById(R.id.tvIngredientNameText)
        val tvIngredientQuantityText: TextView = view.findViewById(R.id.tvIngredientQuantityText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)

        return IngredientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        with(holder) {
            tvIngredientNameText.text = dataset[position].description
            tvIngredientQuantityText.text =
                "${dataset[position].quantity} ${dataset[position].unitOfMeasure}"
        }
    }

    override fun getItemCount() = dataset.size

    fun updateIngredients(progress: Int) {
        dataset.forEach {
            val componentQuantity = it.quantity.toDouble() * progress / quantity

            it.quantity =
                if (isInteger(componentQuantity)) {
                    "${componentQuantity.toInt()}"
                } else {
                    "%.${NUM_OF_INGREDIENT_MANTIS}f".format(locale = Locale.US, componentQuantity)
                }
        }

        quantity = progress

        notifyDataSetChanged()
    }
}

private fun isInteger(num: Double) =
    convertToNumWithNeededAccuracy(num.toInt().toDouble(), NUM_OF_INGREDIENT_MANTIS) ==
            convertToNumWithNeededAccuracy(num, NUM_OF_INGREDIENT_MANTIS)

private fun convertToNumWithNeededAccuracy(num: Double, accuracy: Int) =
    BigDecimal("$num")
        .setScale(accuracy, RoundingMode.HALF_UP)
        .stripTrailingZeros().toPlainString()