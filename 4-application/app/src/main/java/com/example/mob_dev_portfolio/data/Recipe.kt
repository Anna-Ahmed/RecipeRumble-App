package com.example.mob_dev_portfolio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val carbs: Float?,
    val protein: Float?,
    val fat: Float?,
    val ingredients: String?,
    val calories: Float?
)

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val image: String
)

@Entity(tableName = "nutrition")
data class NutritionResponse(
    @PrimaryKey val recipeId: Int,
    val calories: Double
)




@Entity(tableName = "Steps")
data class Step(
    @PrimaryKey val recipeId: Int,
    val number: Int,
    val step: String
)

@Entity(tableName = "favourite_recipes")
data class  FavouriteRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isLiked: Boolean
)
@Entity(tableName = "saved_recipes")
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isSaved: Boolean
)