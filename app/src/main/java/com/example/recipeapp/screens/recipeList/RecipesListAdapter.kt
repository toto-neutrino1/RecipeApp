package com.example.recipeapp.screens.recipeList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe

class RecipesListAdapter(
    private val dataset: List<Recipe>,
    private val fragment: RecipesListFragment
) : RecyclerView.Adapter<RecipesListAdapter.RecipesListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class RecipesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cvListRecipesItem: CardView = view.findViewById(R.id.cvListRecipesItem)
        val ivListRecipesImage: ImageView = view.findViewById(R.id.ivListRecipesImage)
        val tvListRecipesName: TextView = view.findViewById(R.id.tvListRecipesName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_recipes, parent, false)

        return RecipesListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecipesListViewHolder, position: Int) {
        viewHolder.tvListRecipesName.text = dataset[position].title
        try {
            with(viewHolder.ivListRecipesImage) {
                setImageDrawable(
                    Drawable.createFromStream(
                        fragment.context?.assets?.open(dataset[position].imageUrl), null
                    )
                )

                contentDescription =
                    "${fragment.context?.getString(R.string.cont_descr_iv_recipe)}" +
                            "${dataset[position].title}"
            }
        } catch (e: Exception) {
            Log.e(
                "${fragment.context?.getString(R.string.asset_error)}",
                "${e.printStackTrace()}"
            )
        }

        viewHolder.cvListRecipesItem.setOnClickListener {
            itemClickListener?.onItemClick(dataset[position].id)
        }
    }

    override fun getItemCount(): Int = dataset.size
}