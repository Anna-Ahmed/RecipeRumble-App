package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
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
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var cardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModel
        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService, RecipeDatabase.getDatabase(requireContext()))
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView)
        cardView = view.findViewById(R.id.cardView)

        recipeAdapter = RecipeAdapter()
        recipesRecyclerView.layoutManager = LinearLayoutManager(context)
        recipesRecyclerView.adapter = recipeAdapter

        val getRecipesButton: Button = view.findViewById(R.id.getRecipesButton)
        getRecipesButton.setOnClickListener {
            recipeViewModel.fetchRecipes(10, "b94a351e14e94600bc8f1a9cae0008eb")
        }

        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                recipeAdapter.setRecipes(recipes)
                recipesRecyclerView.visibility = View.VISIBLE
                cardView.visibility = View.GONE
            } else {
                recipesRecyclerView.visibility = View.GONE
                cardView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recipes_fragment, container, false)
    }
}