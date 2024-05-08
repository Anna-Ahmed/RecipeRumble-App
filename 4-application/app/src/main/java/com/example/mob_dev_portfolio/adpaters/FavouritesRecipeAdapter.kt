package com.example.mob_dev_portfolio.adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.R

class FavouritesRecipeAdapter(
private val onDeleteClickListener: (FavouriteRecipe) -> Unit,
private val onItemClickListener: (Int) -> Unit) : RecyclerView.Adapter<FavouritesRecipeAdapter.FavouriteRecipeViewHolder>() {

    private var recipes: List<FavouriteRecipe> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourited_recipe, parent, false)
        return FavouriteRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteRecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClickListener(recipe.id)
        }
    }

    fun setRecipes(newRecipes: List<FavouriteRecipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class FavouriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val likeImageView: ImageView = itemView.findViewById(R.id.likeImageView)

        fun bind(recipe: FavouriteRecipe) {
            titleTextView.text = recipe.title


            if (recipe.isLiked) {
                likeImageView.setImageResource(R.drawable.ic_heart_full)
            } else {
                likeImageView.setImageResource(R.drawable.ic_heart_empty)
            }

            likeImageView.setOnClickListener {

                onDeleteClickListener(recipe)
            }
        }
    }
}
