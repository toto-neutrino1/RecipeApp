package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CategoriesListAdapter
    (
    private val dataset: List<Category>,
    private val fragment: CategoriesListFragment
) :
    RecyclerView.Adapter<CategoriesListAdapter.CategoriesListViewHolder>() {

    class CategoriesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cvCategoryItem: CardView = view.findViewById(R.id.cvCategoryItem)
        val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
        val tvCategoryDescription: TextView = view.findViewById(R.id.tvCategoryDescription)
        val ivCategoryImage: ImageView = view.findViewById(R.id.ivCategoryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)

        return CategoriesListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CategoriesListViewHolder, position: Int) {
        viewHolder.tvCategoryName.text = dataset[position].title
        viewHolder.tvCategoryDescription.text = dataset[position].description
        try {
            viewHolder.ivCategoryImage.setImageDrawable(
                Drawable.createFromStream(
                    fragment.context?.assets?.open(dataset[position].imageUrl), null
                )
            )
        } catch (e: Exception) {
            Log.e("onBindViewHolder, categoryImage", e.stackTrace.toString())
        }

        viewHolder.cvCategoryItem.setOnClickListener { }
    }

    override fun getItemCount() = dataset.size
}