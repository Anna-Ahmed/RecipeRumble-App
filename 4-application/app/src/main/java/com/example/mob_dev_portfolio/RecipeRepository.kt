package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData

class RecipeRepository(private val api: SpoonacularAPI, private val dao: RecipeDao) {
    val recipes: LiveData<List<Recipe>> = dao.getAllRecipes()

    suspend fun fetchRecipes(number: Int, apiKey: String) {
        val response = api.getRandomRecipes(number, apiKey)
        if (response.isSuccessful) {
            response.body()?.let {
                for (recipe in it.recipes) {
                    dao.insertRecipe(recipe)
                }
            }
        }
    }
}

