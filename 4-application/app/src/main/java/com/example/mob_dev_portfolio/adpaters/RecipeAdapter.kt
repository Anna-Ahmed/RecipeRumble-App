package com.example.mob_dev_portfolio.adpaters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.main.MainActivity
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.Recipe
import com.example.mob_dev_portfolio.fragments.RecipeDetailFragment

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipes: List<Recipe> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun setRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.recipeTitle)

        fun bind(recipe: Recipe) {
            titleTextView.text = recipe.title
            itemView.setOnClickListener {
                val fragment = RecipeDetailFragment()
                val bundle = Bundle().apply {
                    putInt("recipeId", recipe.id)
                }
                fragment.arguments = bundle
                val fragmentManager = (itemView.context as MainActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }



}