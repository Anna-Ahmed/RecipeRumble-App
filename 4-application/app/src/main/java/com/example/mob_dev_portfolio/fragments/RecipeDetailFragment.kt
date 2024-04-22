package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.repository.RecipeRepository
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.model.ViewModelFactory

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var repository: RecipeRepository
    private var recipeId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.getRecipeAPI()
        val db = RecipeDatabase.getDatabase(requireContext())
        repository = RecipeRepository(apiService, db)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)

        recipeId = arguments?.getInt("recipeId") ?: 0
        if (recipeId > 0) {
            fetchRecipeDetail(recipeId)
        } else {
            Log.e("RecipeDetailFragment", "Invalid recipeId: $recipeId")
        }
        view.findViewById<Button>(R.id.likeButton)?.setOnClickListener {
            recipeViewModel.recipeDetail.value?.let { recipe ->
                val favouriteRecipe = FavouriteRecipe(
                    id = recipe.id,
                    title = recipe.title
                )
                recipeViewModel.addFavouriteRecipe(favouriteRecipe) { id ->
                    if (id > 0) {
                        Toast.makeText(requireContext(), "Recipe added to liked!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add recipe to liked!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }

    private fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe, callback: (Boolean) -> Unit) {
        repository.addFavouriteRecipe(favouriteRecipe) { id ->
            callback(id > 0)
        }
    }

    private fun fetchRecipeDetail(recipeId: Int) {
        val apiKey = "b94a351e14e94600bc8f1a9cae0008eb"

        recipeViewModel.fetchRecipeDetail(recipeId, apiKey)
        recipeViewModel.fetchNutrition(recipeId, apiKey)
        recipeViewModel.fetchAnalyzedInstructions(recipeId, apiKey)

        recipeViewModel.recipeDetail.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                view?.findViewById<TextView>(R.id.titleTextView)?.text = it.title
                view?.findViewById<TextView>(R.id.instructionsContent)?.text = it.instructions

                // Load image using Glide
                view?.findViewById<ImageView>(R.id.recipeImageView)?.let { imageView ->
                    Glide.with(requireContext()).load(it.image).into(imageView)
                }
            }
        }

        recipeViewModel.ingredients.observe(viewLifecycleOwner) { ingredientsList ->
            val ingredientsText = ingredientsList?.joinToString("\n")
            view?.findViewById<TextView>(R.id.ingredientsContent)?.text = ingredientsText
        }

        recipeViewModel.nutrition.observe(viewLifecycleOwner) { nutrition ->
            nutrition?.let {
                view?.findViewById<TextView>(R.id.caloriesContent)?.text =
                    "Calories: ${it.calories}"
            }
        }

        recipeViewModel.recipeInstructions.observe(viewLifecycleOwner) { recipeInstructions ->
            val stepsText = recipeInstructions?.joinToString("\n") { recipeInstructions ->
                recipeInstructions.steps.joinToString("\n") { step ->
                    "${step.number}. ${step.step}"
                }
            }
            view?.findViewById<TextView>(R.id.instructionsContent)?.text = stepsText
        }
    }
}
