package com.example.mob_dev_portfolio.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.repository.RecipeRepository

class ViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

