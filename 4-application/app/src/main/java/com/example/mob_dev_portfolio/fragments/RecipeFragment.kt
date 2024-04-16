package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.R


class RecipeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipes_fragment, container, false)

        val recipeButton: Button = view.findViewById(R.id.getRecipesButton)

        recipeButton.setOnClickListener {
            onRecipeButtonClick()
        }

        return view
    }

    fun onRecipeButtonClick() {
        Toast.makeText(context, "Recipe Button is clicked", Toast.LENGTH_SHORT).show()
    }

}
