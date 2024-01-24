package com.example.recipeapp.screens.categoriesList

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
import com.example.recipeapp.model.Category

class CategoriesListAdapter(
    private val dataset: List<Category>,
    private val fragment: CategoriesListFragment
) : RecyclerView.Adapter<CategoriesListAdapter.CategoriesListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

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
        with(viewHolder) {
            tvCategoryName.text = "${dataset[position].title}"
            tvCategoryDescription.text = "${dataset[position].description}"
        }

        try {
            with(viewHolder.ivCategoryImage) {
                setImageDrawable(
                    Drawable.createFromStream(
                        fragment.context?.assets?.open(dataset[position].imageUrl), null
                    )
                )

                contentDescription =
                    "${fragment.context?.getString(R.string.cont_descr_iv_category)} " +
                            "${dataset[position].title}"
            }
        } catch (e: Exception) {
            Log.e(
                "${fragment.context?.getString(R.string.asset_error)}",
                "${e.printStackTrace()}"
            )
        }

        viewHolder.cvCategoryItem.setOnClickListener {
            itemClickListener?.onItemClick(categoryId = dataset[position].id)
        }
    }

    override fun getItemCount() = dataset.size
}