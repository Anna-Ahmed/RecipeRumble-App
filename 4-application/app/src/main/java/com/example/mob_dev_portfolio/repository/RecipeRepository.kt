package com.example.mob_dev_portfolio.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mob_dev_portfolio.data.FavouriteRecipe
import com.example.mob_dev_portfolio.data.IngredientListResponse
import com.example.mob_dev_portfolio.data.NutritionResponse
import com.example.mob_dev_portfolio.data.Recipe
import com.example.mob_dev_portfolio.data.RecipeAPI
import com.example.mob_dev_portfolio.data.RecipeDatabase
import com.example.mob_dev_portfolio.data.RecipeInstructionResponse
import com.example.mob_dev_portfolio.data.RecipeResponse
import com.example.mob_dev_portfolio.data.SavedRecipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class RecipeRepository(private val api: RecipeAPI, private val db: RecipeDatabase) {


    val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val ingredients: MutableLiveData<List<String>> = MutableLiveData()
    val recipeDetail: MutableLiveData<Recipe?> = MutableLiveData()
    val nutrition: MutableLiveData<NutritionResponse> = MutableLiveData()
    val recipeInstructions: MutableLiveData<List<RecipeInstructionResponse>> = MutableLiveData()
    val favouriteRecipes: LiveData<List<FavouriteRecipe>> = db.recipeDao().getFavouriteRecipes()
    val savedRecipes: LiveData<List<SavedRecipe>> = db.recipeDao().getSavedRecipes()

    fun fetchRecipes(number: Int, apiKey: String) {
        api.getRandomRecipes(number, apiKey).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        recipes.postValue(it.recipes)
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                // Handle failure
                Log.e("RecipeRepository", "Failed to get recipes", t)
            }
        })
    }

    fun getRecipeInformation(recipeId: Int, apiKey: String) {
        api.getRecipeInformation(recipeId, apiKey).enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    recipeDetail.postValue(response.body())

                    api.getIngredientList(recipeId, apiKey).enqueue(object : Callback<IngredientListResponse> {
                        override fun onResponse(call: Call<IngredientListResponse>, ingredientsResponse: Response<IngredientListResponse>) {
                            if (ingredientsResponse.isSuccessful) {
                                ingredients.postValue(ingredientsResponse.body()?.ingredients?.map { it.name })
                            } else {
                                Log.e("RecipeRepository", "API error code: ${ingredientsResponse.code()}")
                                Log.e("RecipeRepository", "API error message: ${ingredientsResponse.message()}")
                            }
                        }

                        override fun onFailure(call: Call<IngredientListResponse>, t: Throwable) {
                            // Handle failure
                            Log.e("RecipeRepository", "Failed to get ingredients", t)
                        }
                    })

                } else {
                    Log.e("RecipeRepository", "API error code: ${response.code()}")
                    Log.e("RecipeRepository", "API error message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                // Handle failure
                Log.e("RecipeRepository", "Failed to get recipe detail", t)
            }
        })
    }

    fun getNutrition(recipeId: Int, apiKey: String) {
        api.getNutrition(recipeId, apiKey).enqueue(object : Callback<NutritionResponse> {
            override fun onResponse(call: Call<NutritionResponse>, response: Response<NutritionResponse>) {
                if (response.isSuccessful) {
                    nutrition.postValue(response.body())
                } else {
                    Log.e("RecipeRepository", "API error code: ${response.code()}")
                    Log.e("RecipeRepository", "API error message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NutritionResponse>, t: Throwable) {
                // Handle failure
                Log.e("RecipeRepository", "Failed to get nutrition facts", t)
            }
        })
    }

    fun getRecipeInstructions(recipeId: Int, apiKey: String) {
        api.getRecipeInstructions(recipeId, apiKey).enqueue(object : Callback<List<RecipeInstructionResponse>> {
            override fun onResponse(call: Call<List<RecipeInstructionResponse>>, response: Response<List<RecipeInstructionResponse>>) {
                if (response.isSuccessful) {
                    recipeInstructions.postValue(response.body())
                } else {
                    Log.e("RecipeRepository", "API error code: ${response.code()}")
                    Log.e("RecipeRepository", "API error message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<RecipeInstructionResponse>>, t: Throwable) {
                Log.e("RecipeRepository", "Failed to get instructions", t)
            }
        })
    }



    fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe, callback: (Long) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            val id = db.recipeDao().insertFavouriteRecipe(favouriteRecipe)

            // Post the result back to the main thread
            Handler(Looper.getMainLooper()).post {
                callback(id)
            }
        }
    }

    fun deleteFavouriteRecipe(favouriteRecipe: FavouriteRecipe) {
        Executors.newSingleThreadExecutor().execute {
            db.recipeDao().deleteFavouriteRecipe(favouriteRecipe)
        }
    }

    fun addSavedRecipe(savedRecipe: SavedRecipe, callback: (Long) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            val id = db.recipeDao().insertSavedRecipe(savedRecipe)
            Handler(Looper.getMainLooper()).post {
                callback(id)
            }
        }
    }

    fun deleteSavedRecipe(savedRecipe: SavedRecipe) {
        Executors.newSingleThreadExecutor().execute {
            db.recipeDao().deleteSavedRecipe(savedRecipe)
        }
    }
    }
