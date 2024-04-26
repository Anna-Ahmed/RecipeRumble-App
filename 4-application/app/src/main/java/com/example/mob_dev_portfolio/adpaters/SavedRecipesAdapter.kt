package com.example.mob_dev_portfolio.adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.SavedRecipe

class SavedRecipesAdapter(
    private val onDeleteClickListener: (SavedRecipe) -> Unit,
    private val onItemClickListener: (Int) -> Unit
) : ListAdapter<SavedRecipe, SavedRecipesAdapter.SavedRecipeViewHolder>(DiffCallback()) {

    class SavedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(savedRecipe: SavedRecipe, onDeleteClickListener: (SavedRecipe) -> Unit) {
            titleTextView.text = savedRecipe.title

            deleteButton.setOnClickListener {
                onDeleteClickListener(savedRecipe)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SavedRecipe>() {
        override fun areItemsTheSame(oldItem: SavedRecipe, newItem: SavedRecipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SavedRecipe, newItem: SavedRecipe): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_recipe, parent, false)
        return SavedRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val savedRecipe = getItem(position)
        savedRecipe?.let {
            holder.bind(it, onDeleteClickListener)
            holder.itemView.setOnClickListener {
                onItemClickListener(savedRecipe.id)
            }
        }
    }
}