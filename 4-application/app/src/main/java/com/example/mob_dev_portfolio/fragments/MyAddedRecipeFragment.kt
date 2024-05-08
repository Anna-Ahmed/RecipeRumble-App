package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.OnItemClickListener
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adpaters.UserRecipeAdapter
import com.example.mob_dev_portfolio.data.MyRecipe
import com.example.mob_dev_portfolio.model.RecipeViewModel

class MyAddedRecipeFragment : Fragment(), OnItemClickListener {

    private lateinit var userRecipeAdapter: UserRecipeAdapter
    private lateinit var userRecipesRecyclerView: RecyclerView
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_recipe_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecipesRecyclerView = view.findViewById(R.id.userRecipesRecyclerView)
        userRecipeAdapter = UserRecipeAdapter(requireContext(), this)

        userRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecipesRecyclerView.adapter = userRecipeAdapter

        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        recipeViewModel.myRecipes.observe(viewLifecycleOwner) { userRecipes ->
            if (userRecipes.isNotEmpty()) {
                userRecipeAdapter.setRecipes(userRecipes)
                userRecipesRecyclerView.visibility = View.VISIBLE
            } else {
                userRecipesRecyclerView.visibility = View.GONE
                Toast.makeText(requireContext(), "No recipes found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(recipe: MyRecipe) {
        val bundle = Bundle().apply {
            putString("title", recipe.title)
            putString("ingredients", recipe.ingredients)
            putString("instructions", recipe.instructions)
            putString("calories", recipe.calories)
        }
        val fragment = MyAddedRecipeDetailFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onRemoveClick(recipe: MyRecipe) {
        recipeViewModel.deleteMyRecipe(recipe)
    }
}

