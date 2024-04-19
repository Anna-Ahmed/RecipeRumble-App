package com.example.mob_dev_portfolio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.fragments.AddRecipeFragment
import com.example.mob_dev_portfolio.fragments.FavouritesFragment
import com.example.mob_dev_portfolio.fragments.MealPlannerFragment
import com.example.mob_dev_portfolio.fragments.RecipeFragment
import com.example.mob_dev_portfolio.fragments.SavedRecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView




class MainActivity : AppCompatActivity() {


    private val recipeFragment = RecipeFragment()
    private val mealPlannerFragment = MealPlannerFragment()
    private val addRecipeFragment = AddRecipeFragment()
    private val savedRecipesFragment = SavedRecipesFragment()
    private val favouritesFragment = FavouritesFragment()

    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigationView()


        val apiService = RetrofitClient.getSpoonacularApiService()
        val recipeDao = AppDatabase.getDatabase(this).recipeDao()
        val repository = RecipeRepository(apiService, recipeDao)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(RecipeViewModel::class.java)


        recipeViewModel.fetchRecipes(10, "45d78e1e234046a68f6aa037628b49")
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
