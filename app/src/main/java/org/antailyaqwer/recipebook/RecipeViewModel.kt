package org.antailyaqwer.recipebook

import androidx.lifecycle.*
import org.antailyaqwer.recipebook.database.RecipeEntity
import org.antailyaqwer.recipebook.database.Repository
import java.util.*

class RecipeViewModel : ViewModel() {

    private val repository = Repository.get()
    private val recipeByIdLiveData = MutableLiveData<UUID>()

    var recipeLiveData: LiveData<RecipeEntity?> =
        Transformations.switchMap(recipeByIdLiveData) { recipeId ->
            repository.getRecipe(recipeId)
        }

    fun loadRecipe(id: UUID) {
        recipeByIdLiveData.value = id
    }

    fun saveRecipe(recipeEntity: RecipeEntity, lifecycleOwner: LifecycleOwner) {
        if (repository.hasRecipe(recipeEntity)) {
            repository.updateRecipe(recipeEntity)
        }
    }
}