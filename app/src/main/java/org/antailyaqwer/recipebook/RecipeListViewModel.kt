package org.antailyaqwer.recipebook

import androidx.lifecycle.ViewModel
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.Repository
import java.util.*

class RecipeListViewModel : ViewModel() {
    private val repository = Repository.get()

    val recipeListLiveData = repository.getAllRecipesOrderedByLastUpdated()

    //TODO Delete 3 useless entities
}