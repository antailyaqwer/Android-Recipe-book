package org.antailyaqwer.recipebook.api

import com.google.gson.annotations.SerializedName
import org.antailyaqwer.recipebook.database.RecipeEntity

data class RecipeResponse(@SerializedName("recipes") var recipes: List<RecipeEntity>)
//{
//    @SerializedName("recipes")
//    lateinit var recipes: List<RecipeEntity>
//}