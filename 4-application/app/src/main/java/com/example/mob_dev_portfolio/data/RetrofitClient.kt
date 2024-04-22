package com.example.mob_dev_portfolio.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.spoonacular.com/"


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getRecipeAPI(): RecipeAPI {
        return retrofit.create(RecipeAPI::class.java)
    }
}


