package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.OnItemClickListener
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adpaters.RecipeAdapter
import com.example.mob_dev_portfolio.adpaters.UserRecipeAdapter
import com.example.mob_dev_portfolio.data.MyRecipe
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.repository.RecipeRepository
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.model.ViewModelFactory


class RecipeFragment : Fragment(), OnItemClickListener {

    private lateinit var mainRecipeAdapter: RecipeAdapter
    private lateinit var userRecipeAdapter: UserRecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var userRecipesRecyclerView: RecyclerView
    private lateinit var viewMyRecipesButton: Button
    private lateinit var getRecipesButton: Button
    private lateinit var cardView: CardView
    private lateinit var cardView1: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService, RecipeDatabase.getDatabase(requireContext()))
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView)
        userRecipesRecyclerView = view.findViewById(R.id.userRecipesRecyclerView)
        viewMyRecipesButton = view.findViewById(R.id.viewMyRecipesButton)
        getRecipesButton = view.findViewById(R.id.getRecipesButton)
        cardView = view.findViewById(R.id.cardView)
        cardView1 = view.findViewById(R.id.cardView1)

        mainRecipeAdapter = RecipeAdapter()
        userRecipeAdapter = UserRecipeAdapter(requireContext(), this)

        recipesRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecipesRecyclerView.layoutManager = LinearLayoutManager(context)

        recipesRecyclerView.adapter = mainRecipeAdapter
        userRecipesRecyclerView.adapter = userRecipeAdapter

        getRecipesButton.setOnClickListener {

            recipeViewModel.fetchRecipes(25, "b94a351e14e94600bc8f1a9cae0008eb")
        }

        viewMyRecipesButton.setOnClickListener {

            displayUserRecipes()
        }

        viewMyRecipesButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MyAddedRecipeFragment())
                .addToBackStack(null)
                .commit()
        }


        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                mainRecipeAdapter.setRecipes(recipes)
                recipesRecyclerView.visibility = View.VISIBLE
                userRecipesRecyclerView.visibility = View.GONE

                viewMyRecipesButton.visibility = View.GONE
                getRecipesButton.visibility = View.GONE
                cardView.visibility = View.GONE
                cardView1.visibility = View.GONE
            } else {
                recipesRecyclerView.visibility = View.GONE
                userRecipesRecyclerView.visibility = View.GONE

                viewMyRecipesButton.visibility = View.VISIBLE
                getRecipesButton.visibility = View.VISIBLE
                cardView.visibility = View.VISIBLE
                cardView1.visibility = View.VISIBLE
            }
        }
    }

    private fun displayUserRecipes() {
        recipeViewModel.myRecipes.observe(viewLifecycleOwner) { userRecipes ->
            if (userRecipes.isNotEmpty()) {
                userRecipeAdapter.setRecipes(userRecipes)
                userRecipesRecyclerView.visibility = View.VISIBLE
                recipesRecyclerView.visibility = View.GONE
            } else {
                // Handle case when no user recipes are found
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recipes_fragment, container, false)
    }

    override fun onRemoveClick(recipe: MyRecipe) {
        recipeViewModel.deleteMyRecipe(recipe)
    }
}