package com.example.mob_dev_portfolio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeRepository(private val api: RecipeAPI) {

    val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val ingredients: MutableLiveData<List<String>> = MutableLiveData()
    val recipeDetail: MutableLiveData<Recipe?> = MutableLiveData()

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
                Log.e("RecipeRepository", "Failed to fetch recipes", t)
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
                            Log.e("RecipeRepository", "Failed to fetch ingredients", t)
                        }
                    })

                } else {
                    Log.e("RecipeRepository", "API error code: ${response.code()}")
                    Log.e("RecipeRepository", "API error message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                // Handle failure
                Log.e("RecipeRepository", "Failed to fetch recipe detail", t)
            }
        })
    }
}
