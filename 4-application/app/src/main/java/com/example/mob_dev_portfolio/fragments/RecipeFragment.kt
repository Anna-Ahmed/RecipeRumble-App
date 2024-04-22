package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adpaters.RecipeAdapter
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.repository.RecipeRepository
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.model.ViewModelFactory


class RecipeFragment: Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var db: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = RecipeDatabase.getDatabase(requireContext())
        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService, db)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getRecipesButton: Button = view.findViewById(R.id.getRecipesButton)
        getRecipesButton.setOnClickListener {
            recipeViewModel.fetchRecipes(10, "b94a351e14e94600bc8f1a9cae0008eb")
        }

        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                // Display RecyclerView and hide the button
                view.findViewById<RecyclerView>(R.id.recipesRecyclerView).visibility = View.VISIBLE
                getRecipesButton.visibility = View.GONE
                recipeAdapter.setRecipes(recipes)
            }
        }
    }

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

        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.setRecipes(recipes)
        }

        return view
    }
}
