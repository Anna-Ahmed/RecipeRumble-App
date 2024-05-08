package com.example.mob_dev_portfolio.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.data.RetrofitClient
import com.example.mob_dev_portfolio.fragments.AddRecipeFragment
import com.example.mob_dev_portfolio.fragments.FavouritesFragment
import com.example.mob_dev_portfolio.fragments.MealPlannerFragment
import com.example.mob_dev_portfolio.fragments.RecipeFragment
import com.example.mob_dev_portfolio.fragments.SavedRecipesFragment
import com.example.mob_dev_portfolio.model.RecipeViewModel
import com.example.mob_dev_portfolio.model.ViewModelFactory
import com.example.mob_dev_portfolio.repository.RecipeRepository
import com.google.android.material.bottomnavigation.BottomNavigationView




class MainActivity : AppCompatActivity() {

    private val recipeFragment = RecipeFragment()
    private val mealPlannerFragment = MealPlannerFragment()
    private val addRecipeFragment = AddRecipeFragment()
    private val savedRecipesFragment = SavedRecipesFragment()
    private val favouritesFragment = FavouritesFragment()

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var db: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        db = RecipeDatabase.getDatabase(applicationContext)


        val apiService = RetrofitClient.getRecipeAPI()
        val repository = RecipeRepository(apiService, db)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.recipe_nav -> {
                    replaceFragment(recipeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.meal_plan_nav -> {
                    replaceFragment(mealPlannerFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_recipe_nav -> {
                    replaceFragment(addRecipeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.saved_recipes_nav -> {
                    replaceFragment(savedRecipesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourites_nav -> {
                    replaceFragment(favouritesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

