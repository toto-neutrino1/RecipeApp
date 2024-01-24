package com.example.recipeapp.screens.recipe

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorationDivider(private val drawable: Drawable?) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let {
            val viewAdapterPosition = parent.getChildAdapterPosition(view)
            rect.bottom = when (viewAdapterPosition) {
                it.itemCount - 1, RecyclerView.NO_POSITION -> 0
                else -> drawable?.intrinsicHeight ?: 1
            }
        }
    }
}