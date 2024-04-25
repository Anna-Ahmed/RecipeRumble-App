package com.example.mob_dev_portfolio.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.data.NutritionResponse
import com.example.mob_dev_portfolio.data.Recipe
import com.example.mob_dev_portfolio.data.RecipeInstructionResponse
import com.example.mob_dev_portfolio.repository.RecipeRepository

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    val recipes: LiveData<List<Recipe>> = repository.recipes
    val ingredients: LiveData<List<String>> = repository.ingredients
    val recipeDetail: LiveData<Recipe?> = repository.recipeDetail
    val nutrition: LiveData<NutritionResponse> = repository.nutrition
    val recipeInstructions: LiveData<List<RecipeInstructionResponse>> = repository.recipeInstructions
    val favouriteRecipes: LiveData<List<FavouriteRecipe>> = repository.favouriteRecipes
    fun fetchRecipes(number: Int, apiKey: String) {
        repository.fetchRecipes(number, apiKey)
    }

    fun fetchRecipeDetail(recipeId: Int, apiKey: String) {
        repository.getRecipeInformation(recipeId, apiKey)
    }

    fun fetchNutrition(recipeId: Int, apiKey: String) {
        repository.getNutrition(recipeId, apiKey)
    }

    fun fetchAnalyzedInstructions(recipeId: Int, apiKey: String) {
        repository.getRecipeInstructions(recipeId, apiKey)
    }

    fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe, callback: (Long) -> Unit) {
        repository.addFavouriteRecipe(favouriteRecipe, callback)
    }
    fun deleteFavouriteRecipe(favouriteRecipe: FavouriteRecipe) {
        repository.deleteFavouriteRecipe(favouriteRecipe)
    }
}