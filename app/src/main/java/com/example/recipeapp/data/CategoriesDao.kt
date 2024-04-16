package com.example.recipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM $CATEGORY")
    fun getCategories(): List<Category>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(categories: List<Category>)
}