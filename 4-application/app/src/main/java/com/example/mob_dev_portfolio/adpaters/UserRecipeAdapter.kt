package com.example.mob_dev_portfolio.adpaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.OnItemClickListener
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.MyRecipe

class UserRecipeAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<UserRecipeAdapter.UserRecipeViewHolder>() {

    private var recipes: List<MyRecipe> = listOf()

    fun setRecipes(recipes: List<MyRecipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRecipeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_recipe_item, parent, false)
        return UserRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserRecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.findViewById<ImageButton>(R.id.removeRecipeButton).setOnClickListener {
            itemClickListener.onRemoveClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
    inner class UserRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeTitleTextView: TextView = itemView.findViewById(R.id.myRecipeTitle)
        private val removeButton: ImageButton = itemView.findViewById(R.id.removeRecipeButton)

        fun bind(recipe: MyRecipe) {
            recipeTitleTextView.text = recipe.title
            itemView.setOnClickListener {
                itemClickListener.onItemClick(recipe)
            }


            removeButton.setOnClickListener {
                itemClickListener.onRemoveClick(recipe)
            }
        }
    }

}

