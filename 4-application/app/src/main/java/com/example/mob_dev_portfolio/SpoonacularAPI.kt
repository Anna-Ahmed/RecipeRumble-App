package com.example.mob_dev_portfolio


import retrofit2.Call;
import retrofit2.Response
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface SpoonacularAPI {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String
    ): Response<RecipeResponse>
}

data class RecipeResponse(val recipes: List<Recipe>)