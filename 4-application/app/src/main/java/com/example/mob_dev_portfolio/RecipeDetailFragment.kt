package com.example.mob_dev_portfolio

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeViewModel: RecipeViewModel

    private var recipeId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        // Initialize ViewModel
        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)

        // Retrieve recipeId from arguments
        recipeId = arguments?.getInt("recipeId") ?: 0
        if (recipeId > 0) {
            displayRecipeDetail(recipeId)
        } else {
            Log.e("RecipeDetailFragment", "Invalid recipeId: $recipeId")
        }

        return view
    }

    // displaying recipe details
    private fun displayRecipeDetail(recipeId: Int) {
        val apiKey = "b94a351e14e94600bc8f1a9cae0008eb"
        recipeViewModel.fetchRecipeDetail(recipeId, apiKey)

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
    }

}