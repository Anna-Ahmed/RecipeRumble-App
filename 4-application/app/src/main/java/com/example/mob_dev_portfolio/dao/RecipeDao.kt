package com.example.mob_dev_portfolio.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.data.MyRecipe
import com.example.mob_dev_portfolio.data.SavedRecipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM favourite_recipes")
    fun getFavouriteRecipes(): LiveData<List<FavouriteRecipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavouriteRecipe(favouriteRecipeRecipe: FavouriteRecipe): Long

    @Delete
    fun deleteFavouriteRecipe(recipe: FavouriteRecipe)
    @Query("SELECT * FROM saved_recipes")
    fun getSavedRecipes(): LiveData<List<SavedRecipe>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSavedRecipe(savedRecipe: SavedRecipe): Long

    @Delete
    fun deleteSavedRecipe(savedRecipe: SavedRecipe)

    @Query("SELECT * FROM my_recipes")
    fun getMyRecipes(): LiveData<List<MyRecipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMyRecipe(myRecipe: MyRecipe): Long
    @Delete
    fun deleteMyRecipe(myRecipe: MyRecipe)
}