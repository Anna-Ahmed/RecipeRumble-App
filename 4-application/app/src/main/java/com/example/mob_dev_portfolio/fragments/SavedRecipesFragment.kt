package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adpaters.SavedRecipesAdapter
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.model.ViewModelFactory
import com.example.mob_dev_portfolio.repository.RecipeRepository

class SavedRecipesFragment: Fragment() {

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var savedRecipesAdapter: SavedRecipesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService, RecipeDatabase.getDatabase(requireContext()))
        recipeViewModel =
            ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)


        savedRecipesAdapter = SavedRecipesAdapter(
            { savedRecipe ->
                recipeViewModel.deleteSavedRecipe(savedRecipe)
            },
            { recipeId ->
                navigateToRecipeDetail(recipeId)
            }
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.savedRecipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = savedRecipesAdapter


        recipeViewModel.savedRecipes.observe(viewLifecycleOwner, Observer { savedRecipes ->
            savedRecipes?.let {
                savedRecipesAdapter.submitList(it)
            }
        })
    }



    private fun navigateToRecipeDetail(recipeId: Int) {

        val fragmentContainer = activity?.findViewById<View>(R.id.fragmentContainer)
        fragmentContainer?.let {
            val bundle = Bundle()
            bundle.putInt("recipeId", recipeId)
            val recipeDetailFragment = RecipeDetailFragment()
            recipeDetailFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, recipeDetailFragment)
                .addToBackStack(null)
                .commit()
        } ?: run {
            Toast.makeText(requireContext(), "Fragment container not found", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_recipes_fragment, container, false)
    }

}