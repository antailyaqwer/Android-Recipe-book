package org.antailyaqwer.recipebook

import androidx.lifecycle.*
import org.antailyaqwer.recipebook.api.ParserRepository
import org.antailyaqwer.recipebook.database.Repository

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()
    var recipeListLiveData = repository.getAllRecipesOrderedByNameAscending()

    fun parseObjects() {
        ParserRepository().getRecipes()
    }

    fun searchByNameOrDescriptionOrInstructions(query: String) =
        repository.searchByNameOrDescriptionOrInstructions(query)

    fun getAllRecipesByNameAscending() =
        repository.getAllRecipesOrderedByNameAscending()

    fun getAllRecipesByNameDescending() =
        repository.getAllRecipesOrderedByNameDescending()
}