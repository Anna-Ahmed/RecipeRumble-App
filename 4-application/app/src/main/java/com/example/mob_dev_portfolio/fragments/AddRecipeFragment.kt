package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.MyRecipe
import com.example.mob_dev_portfolio.model.RecipeViewModel

class AddRecipeFragment: Fragment() {


    private lateinit var addRecipeButton: Button
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        addRecipeButton = view.findViewById(R.id.addRecipeButton)
        addRecipeButton.setOnClickListener {
            addRecipe()
        }
    }

    private fun addRecipe() {
        val recipeNameEditText: EditText? = view?.findViewById(R.id.recipeNameEditText)
        val ingredientsEditText: EditText? = view?.findViewById(R.id.ingredientsEditText)
        val instructionsEditText: EditText? = view?.findViewById(R.id.instructionsEditText)
        val caloriesEditText: EditText? = view?.findViewById(R.id.caloriesEditText)

        val recipeName = recipeNameEditText?.text.toString()
        val ingredients = ingredientsEditText?.text.toString()
        val instructions = instructionsEditText?.text.toString()
        val calories = caloriesEditText?.text.toString()


        if (recipeName.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || calories.isEmpty()) {

            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }


        val myRecipe = MyRecipe(
            id = 0,
            title = recipeName,
            instructions = instructions,
            ingredients = ingredients,
            calories = calories
        )


        recipeViewModel.addMyRecipe(myRecipe) { id ->
            if (id != -1L) {
                Toast.makeText(requireContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(requireContext(), "Failed to add recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_recipe_fragment, container, false)
    }
}