package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.repository.RecipeRepository
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.data.SavedRecipe
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
        recipeViewModel =
            ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)


        recipeId = arguments?.getInt("recipeId") ?: 0
        if (recipeId > 0) {
            fetchRecipeDetail(recipeId)
            setIconForFavouriteRecipes(recipeId)
            setIconForSavedRecipes(recipeId)

        } else {
            Log.e("RecipeDetailFragment", "Invalid recipeId: $recipeId")
        }


        view.findViewById<ImageView>(R.id.likeImageView)?.setOnClickListener {
            handleLikeButtonClick()
        }

        view.findViewById<ImageView>(R.id.bookmarkImageView)?.setOnClickListener {
            handleBookmarkButtonClick()
        }
        // Handling backButton click
        view.findViewById<ImageButton>(R.id.backButtonAPI).setOnClickListener {
            activity?.onBackPressed() // Navigate back to the previous screen
        }

    }

    private fun handleLikeButtonClick() {
        recipeViewModel.recipeDetail.value?.let { recipe ->
            val isCurrentRecipeLiked = recipeViewModel.favouriteRecipes.value?.any { it.id == recipeId } ?: false

            if (isCurrentRecipeLiked) {

                recipeViewModel.deleteFavouriteRecipe(FavouriteRecipe(id = recipe.id, title = recipe.title, isLiked = false))
                view?.findViewById<ImageView>(R.id.likeImageView)?.setImageResource(R.drawable.ic_heart_empty)
            } else {

                recipeViewModel.addFavouriteRecipe(FavouriteRecipe(id = recipe.id, title = recipe.title, isLiked = true)) { id ->
                    if (id > 0) {
                        Toast.makeText(
                            requireContext(),
                            "Recipe added to liked!",
                            Toast.LENGTH_SHORT
                        ).show()

                        view?.findViewById<ImageView>(R.id.likeImageView)?.setImageResource(R.drawable.ic_heart_full)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to add recipe to liked!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun handleBookmarkButtonClick() {
        recipeViewModel.recipeDetail.value?.let { recipe ->
            val isCurrentRecipeSaved = recipeViewModel.savedRecipes.value?.any { it.id == recipeId } ?: false

            if (isCurrentRecipeSaved) {

                recipeViewModel.deleteSavedRecipe(SavedRecipe(
                    id = recipe.id,
                    title = recipe.title,
                    isSaved = false
                ))
                view?.findViewById<ImageView>(R.id.bookmarkImageView)?.setImageResource(R.drawable.ic_bookmark_empty)
            } else {

                recipeViewModel.addSavedRecipe(
                    SavedRecipe(
                    id = recipe.id,
                    title = recipe.title,
                    isSaved = true
                )
                ) { id ->
                    if (id > 0) {
                        Toast.makeText(
                            requireContext(),
                            "Recipe added to saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                        view?.findViewById<ImageView>(R.id.bookmarkImageView)?.setImageResource(R.drawable.ic_bookmark_full)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to add recipe to saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun setIconForFavouriteRecipes(recipeId: Int) {
        recipeViewModel.favouriteRecipes.observe(viewLifecycleOwner, Observer { favouriteRecipes ->
            val isCurrentRecipeLiked = favouriteRecipes.any { it.id == recipeId }


            if (isCurrentRecipeLiked) {

                view?.findViewById<ImageView>(R.id.likeImageView)?.setImageResource(R.drawable.ic_heart_full)
            } else {
                view?.findViewById<ImageView>(R.id.likeImageView)?.setImageResource(R.drawable.ic_heart_empty)
            }
        })
    }
    private fun setIconForSavedRecipes(recipeId: Int) {
        recipeViewModel.savedRecipes.observe(viewLifecycleOwner, Observer { savedRecipes ->
            val isCurrentRecipeSaved = savedRecipes.any { it.id == recipeId }


            if (isCurrentRecipeSaved) {

                view?.findViewById<ImageView>(R.id.bookmarkImageView)?.setImageResource(R.drawable.ic_bookmark_full)
            } else {

                view?.findViewById<ImageView>(R.id.bookmarkImageView)?.setImageResource(R.drawable.ic_bookmark_empty)
            }
        })
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
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
            val stepsText = recipeInstructions?.joinToString("\n") { recipeInstruction ->
                recipeInstruction.steps.joinToString("\n") { step ->
                    "• ${step.step}"
                }
            }
            view?.findViewById<TextView>(R.id.instructionsContent)?.text = stepsText
        }
    }
}
