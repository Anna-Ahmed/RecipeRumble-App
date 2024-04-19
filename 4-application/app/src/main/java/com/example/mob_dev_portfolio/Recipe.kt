package com.example.mob_dev_portfolio

import androidx.room.PrimaryKey

data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String
)