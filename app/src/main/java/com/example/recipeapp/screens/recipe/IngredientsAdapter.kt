package com.example.recipeapp.screens.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.model.Ingredient

class IngredientsAdapter(
    private val dataset: List<Ingredient>,
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
}