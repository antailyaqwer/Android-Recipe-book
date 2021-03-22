package org.antailyaqwer.recipebook

import androidx.lifecycle.*
import org.antailyaqwer.recipebook.api.ParserRepository
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.Repository

private const val TAG = "RecipeListViewModel"

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()
    val recipeListLiveData = repository.getAllRecipesOrderedByNameAscending()
    private lateinit var mLiveData: MutableLiveData<List<RecipeEntity>>

    fun parseObjects() {
        ParserRepository().getRecipes()
    }
}