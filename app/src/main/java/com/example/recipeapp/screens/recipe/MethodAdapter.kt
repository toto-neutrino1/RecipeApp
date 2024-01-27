package com.example.recipeapp.screens.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import java.text.FieldPosition

class MethodAdapter(
    private val dataset: List<String>
) : RecyclerView.Adapter<MethodAdapter.MethodViewHolder>() {

    class MethodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMethodText: TextView = view.findViewById(R.id.tvMethodText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_method, parent, false)

        return MethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.tvMethodText.text = "${position + 1}. ${dataset[position]}"
    }

    override fun getItemCount() = dataset.size
}