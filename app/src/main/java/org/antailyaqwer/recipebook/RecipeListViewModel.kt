package org.antailyaqwer.recipebook

import androidx.lifecycle.ViewModel
import org.antailyaqwer.recipebook.database.Repository

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()
    val recipeListLiveData = repository.getAllRecipesOrderedByDateAscending()

    //TODO Delete 3 useless entities
    fun parseObjects() = repository.parseObjects()
}