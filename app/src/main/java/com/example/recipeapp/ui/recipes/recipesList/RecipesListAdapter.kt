package com.example.recipeapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipesListAdapter(
    private val dataset: List<Recipe>,
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
        try {
            with(viewHolder.binding.ivListRecipesImage) {
                val inputStream =
                    viewHolder.itemView.context.assets.open(dataset[position].imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                setImageDrawable(drawable)

                contentDescription =
                    viewHolder.itemView.context.getString(R.string.cont_descr_iv_recipe) +
                            dataset[position].title
            }
        } catch (e: Exception) {
            Log.e(
                viewHolder.itemView.context.getString(R.string.asset_error),
                "${e.printStackTrace()}"
            )
        }

        viewHolder.binding.cvListRecipesItem.setOnClickListener {
            itemClickListener?.onItemClick(dataset[position].id)
        }
    }

    override fun getItemCount(): Int = dataset.size
}