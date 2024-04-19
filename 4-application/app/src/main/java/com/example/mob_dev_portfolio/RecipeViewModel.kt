package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    val recipes: LiveData<List<Recipe>> = repository.recipes

    fun fetchRecipes(number: Int, apiKey: String) {
        viewModelScope.launch {
            repository.fetchRecipes(number, apiKey)
        }
    }
}