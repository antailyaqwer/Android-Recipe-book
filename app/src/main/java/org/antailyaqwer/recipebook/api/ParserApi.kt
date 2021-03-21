package org.antailyaqwer.recipebook.api

import org.antailyaqwer.recipebook.database.RecipeEntity
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface ParserApi {

    //TODO возможно, поменять на /recipes.json
    @GET("/recipes")
//    fun getRecipes(): Call<List<RecipeEntity>>
    fun getRecipes(): Call<RecipeResponse>

//    @GET("/recipes/:uuid")
//    fun getRecipe(uuid: UUID): Call<RecipeEntity>
}