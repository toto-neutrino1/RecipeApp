package com.example.recipeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    var quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable