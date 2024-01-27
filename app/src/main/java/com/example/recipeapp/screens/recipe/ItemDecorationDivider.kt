package com.example.recipeapp.screens.recipe

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class ItemDecorationDivider(private val drawable: Drawable?) : RecyclerView.ItemDecoration() {

    private val dividerHeight = drawable?.intrinsicHeight ?: 1

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.let { adapter ->
            parent.children.forEach { view ->
                val viewAdapterPosition = parent.getChildAdapterPosition(view)
                    .let {
                        if (it == RecyclerView.NO_POSITION) return else it
                    }

                if (viewAdapterPosition != adapter.itemCount - 1) {
                    val left = view.left
                    val top = view.bottom
                    val right = view.right
                    val bottom = top + dividerHeight

                    drawable?.bounds = Rect(left, top, right, bottom)
                    drawable?.draw(c)
                }
            }
        }
    }
}