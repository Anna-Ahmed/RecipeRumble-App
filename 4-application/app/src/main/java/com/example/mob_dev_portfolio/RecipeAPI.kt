package com.example.mob_dev_portfolio


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
}


data class RecipeResponse(val recipes: List<Recipe>
)

data class IngredientListResponse(
    val ingredients: List<Ingredient>
)



data class Amount(
    val unit: String,
    val value: Double
)

data class Ingredient(
    val amount: Amount,
    val name: String,
    val image: String
)