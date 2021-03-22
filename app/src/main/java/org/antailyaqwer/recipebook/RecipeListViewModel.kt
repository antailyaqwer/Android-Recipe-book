package org.antailyaqwer.recipebook

import androidx.lifecycle.*
import org.antailyaqwer.recipebook.api.ParserRepository
import org.antailyaqwer.recipebook.database.Repository

private const val TAG = "RecipeListViewModel"

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()
    var recipeListLiveData = repository.getAllRecipesOrderedByNameAscending()
//    private lateinit var mLiveData: MutableLiveData<List<RecipeEntity>>

    fun parseObjects() {
        ParserRepository().getRecipes()
    }

    fun searchByNameOrDescriptionOrInstructions(query: String) =
        repository.searchByNameOrDescriptionOrInstructions(query)

    fun getAllRecipesByNameAscending() =
        repository.getAllRecipesOrderedByNameAscending()
}