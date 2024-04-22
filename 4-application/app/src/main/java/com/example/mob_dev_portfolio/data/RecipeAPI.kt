package com.example.mob_dev_portfolio.data


import retrofit2.Call
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface RecipeAPI {
    @GET("recipes/random")
    fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String
    ): Call<RecipeResponse>

    @GET("recipes/{id}/information")
    fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Call<Recipe>

    @GET("recipes/{id}/ingredientWidget.json")
    fun getIngredientList(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Call<IngredientListResponse>

    @GET("recipes/{id}/nutritionWidget.json")
    fun getNutrition(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Call<NutritionResponse>

    @GET("recipes/{id}/analyzedInstructions")
    fun getRecipeInstructions(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Call<List<RecipeInstructionResponse>>
}


data class RecipeResponse(val recipes: List<Recipe>
)

data class IngredientListResponse(
    val ingredients: List<Ingredient>
)


data class RecipeInstructionResponse(
    val steps: List<Step>
)
