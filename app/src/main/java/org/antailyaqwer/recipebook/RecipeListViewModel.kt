package org.antailyaqwer.recipebook

import androidx.lifecycle.ViewModel
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.Repository
import java.util.*

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()

    //TODO get All Recipes.
    val recipeListLiveData = repository.getAllRecipesOrderedByLastUpdated()

    fun tempFun() {
        val tempRepository = Repository.get()
        val list = listOf("1")
        val date = Date()
        val recipe = RecipeEntity(
            UUID.randomUUID(),
            "1",
            list,
            date,
            "1",
            "2",
            1
        )
        tempRepository.addRecipe(recipe)
    }
}