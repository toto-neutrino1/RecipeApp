package com.example.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(
    var dataset: List<String>
) : RecyclerView.Adapter<MethodAdapter.MethodViewHolder>() {

    class MethodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMethodBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_method, parent, false)

        return MethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.binding.tvMethodText.text = "${position + 1}. ${dataset[position]}"
    }

    override fun getItemCount() = dataset.size
}