package org.antailyaqwer.recipebook.database

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "recipe-database"

class Repository private constructor(context: Context) {

    private val database: RecipeDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecipeDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val recipeDao = database.recipeDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getRecipe(id: UUID): LiveData<RecipeEntity?> = recipeDao.getRecipe(id)

    fun getAllRecipesOrderedByName(sortType: Boolean = false): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByName(sortType)

    fun getAllRecipesOrderedByLastUpdated(sortType: Boolean = false): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByLastUpdated(sortType)

    fun updateRecipe(recipe: RecipeEntity) {
        executor.execute {
            recipeDao.updateRecipe(recipe)
        }
    }

    fun addRecipe(recipe: RecipeEntity) {
        executor.execute {
            recipeDao.addRecipe(recipe)
        }
    }

    fun hasRecipe(recipe: RecipeEntity, lifecycleOwner: LifecycleOwner): Boolean {
        var temp = false
        executor.execute {
            getAllRecipesOrderedByName().observe(
                lifecycleOwner,
                { _recipe ->
                    if (_recipe.any {
                            recipe.id == it.id
                        }) temp = true
                }
            )
        }
        return temp
    }

    fun needUpdate(recipe: RecipeEntity, lifecycleOwner: LifecycleOwner): Boolean {
        var temp = false
        executor.execute {
            getAllRecipesOrderedByName().observe(
                lifecycleOwner,
                { _recipe ->
                    if (_recipe.any {
                            recipe.id == it.id && recipe.lastUpdated > it.lastUpdated
                        }) temp = true
                }
            )
        }
        return temp
    }

    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = Repository(context)
        }

        fun get(): Repository =
            INSTANCE ?: throw IllegalStateException("Repository must be initialized")
    }
}