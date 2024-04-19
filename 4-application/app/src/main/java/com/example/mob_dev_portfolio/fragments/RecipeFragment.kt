package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.AppDatabase
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.RecipeAdapter
import com.example.mob_dev_portfolio.RecipeRepository
import com.example.mob_dev_portfolio.RecipeViewModel
import com.example.mob_dev_portfolio.RetrofitClient
import com.example.mob_dev_portfolio.ViewModelFactory


class RecipeFragment: Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipes_fragment, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recipeAdapter = RecipeAdapter()
        recyclerView.adapter = recipeAdapter

        // Initialize ViewModel
        val apiService = RetrofitClient.getSpoonacularApiService()
        val recipeDao = AppDatabase.getDatabase(requireContext()).recipeDao()
        val repository = RecipeRepository(apiService, recipeDao)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)

        recipeViewModel.recipes.observe(viewLifecycleOwner, { recipes ->
            recipeAdapter.setRecipes(recipes)
        })

        return view
    }
}