package com.example.mob_dev_portfolio

import com.example.mob_dev_portfolio.data.MyRecipe

interface OnItemClickListener {
    fun onItemClick(recipe: MyRecipe)
    fun onRemoveClick(recipe: MyRecipe)
}