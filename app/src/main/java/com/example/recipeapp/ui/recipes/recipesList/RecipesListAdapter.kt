package com.example.recipeapp.ui.recipes.recipesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipesListAdapter(
    var dataset: List<Recipe>,
) : RecyclerView.Adapter<RecipesListAdapter.RecipesListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class RecipesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecipeBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)

        return RecipesListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecipesListViewHolder, position: Int) {
        viewHolder.binding.tvListRecipesName.text = dataset[position].title

        with(viewHolder.binding.ivListRecipesImage) {
            Glide.with(context)
                .load(dataset[position].imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(this)

            contentDescription =
                viewHolder.itemView.context.getString(R.string.cont_descr_iv_recipe) +
                        dataset[position].title
        }

        viewHolder.binding.cvListRecipesItem.setOnClickListener {
            itemClickListener?.onItemClick(dataset[position].id)
        }
    }

    override fun getItemCount(): Int = dataset.size
}