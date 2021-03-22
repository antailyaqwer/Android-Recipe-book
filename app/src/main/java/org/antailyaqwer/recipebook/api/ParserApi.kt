package org.antailyaqwer.recipebook.api

import retrofit2.Call
import retrofit2.http.GET

interface ParserApi {

    //TODO возможно, поменять на /recipes.json
    @GET("/recipes")
//    fun getRecipes(): Call<List<RecipeEntity>>
    fun getRecipes(): Call<RecipeResponse>

//    @GET("/recipes/:uuid")
//    fun getRecipe(uuid: UUID): Call<RecipeEntity>
}