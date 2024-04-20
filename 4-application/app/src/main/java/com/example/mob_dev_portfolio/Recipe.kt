package com.example.mob_dev_portfolio

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val ingredients: List<String>?
)