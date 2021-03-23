package org.antailyaqwer.recipebook.api

import retrofit2.Call
import retrofit2.http.GET

interface ParserApi {

    @GET("/recipes")
    fun getRecipes(): Call<RecipeResponse>
}