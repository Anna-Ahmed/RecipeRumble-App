package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = repository.recipes
    val ingredients: LiveData<List<String>> = repository.ingredients
    val recipeDetail: LiveData<Recipe?> = repository.recipeDetail

    fun fetchRecipes(number: Int, apiKey: String) {
        repository.fetchRecipes(number, apiKey)
    }

    fun fetchRecipeDetail(recipeId: Int, apiKey: String) {
        repository.getRecipeInformation(recipeId, apiKey)
    }
}

