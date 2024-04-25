package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adpaters.FavouritesRecipeAdapter
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.model.ViewModelFactory
import com.example.mob_dev_portfolio.repository.RecipeRepository

class FavouritesFragment: Fragment() {
    private lateinit var favouriteRecipeAdapter: FavouritesRecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val apiService = RetrofitClient.getRecipeAPI()
        val db = RecipeDatabase.getDatabase(requireContext())
        val repository = RecipeRepository(apiService, db)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.favouriteRecipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        favouriteRecipeAdapter = FavouritesRecipeAdapter(
            { favouriteRecipe ->
                recipeViewModel.deleteFavouriteRecipe(favouriteRecipe)
            },
            { recipeId ->
                val bundle = Bundle()
                bundle.putInt("recipeId", recipeId)
                val recipeDetailFragment = RecipeDetailFragment()
                recipeDetailFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, recipeDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        recyclerView.adapter = favouriteRecipeAdapter

        recipeViewModel.favouriteRecipes.observe(viewLifecycleOwner) { favouriteRecipes ->
            favouriteRecipes?.let {
               favouriteRecipeAdapter.setRecipes(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }
}

