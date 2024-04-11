package com.example.recipeapp.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemCategoryBinding
import com.example.recipeapp.model.Category

class CategoriesListAdapter(
    var dataset: List<Category>
) : RecyclerView.Adapter<CategoriesListAdapter.CategoriesListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class CategoriesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCategoryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)

        return CategoriesListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CategoriesListViewHolder, position: Int) {
        with(viewHolder.binding) {
            tvCategoryName.text = dataset[position].title
            tvCategoryDescription.text = dataset[position].description
        }

        with(viewHolder.binding.ivCategoryImage) {
            Glide.with(context)
                .load(dataset[position].imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(this)

            contentDescription =
                "${viewHolder.itemView.context.getString(R.string.cont_descr_iv_category)} " +
                        dataset[position].title
        }

        viewHolder.binding.cvCategoryItem.setOnClickListener {
            itemClickListener?.onItemClick(categoryId = dataset[position].id)
        }
    }

    override fun getItemCount() = dataset.size
}