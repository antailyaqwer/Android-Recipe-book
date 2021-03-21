package org.antailyaqwer.recipebook.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Room
import org.antailyaqwer.recipebook.api.ParserRepository
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

    //TODO Возможно, переместить парсинг отсюда
    private val parserRepository = ParserRepository()
    fun parseObjects() {
        parserRepository.getRecipes().map {
            it.forEach { recipe ->
                Log.d("Repository", "trying to fetch recipes")
                addRecipe(recipe)
            }
        }
    }

    fun getRecipe(id: UUID): LiveData<RecipeEntity?> = recipeDao.getRecipe(id)

    fun getAllRecipesOrderedByNameAscending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByNameAscending()

    fun getAllRecipesOrderedByDateAscending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByDateAscending()

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

    fun hasRecipe(recipe: RecipeEntity): Boolean {
        var temp = false
        executor.execute {
            getAllRecipesOrderedByNameAscending().map {
                if (it.any { _recipe ->
                        recipe.uuid == _recipe.uuid
                    }) temp = true
            }
        }
        return temp
    }

    fun needUpdate(recipe: RecipeEntity, lifecycleOwner: LifecycleOwner): Boolean {
        var temp = false
        executor.execute {
            getAllRecipesOrderedByNameAscending().map {
                if (it.any { _recipe ->
                        recipe.uuid == _recipe.uuid && recipe.lastUpdated > _recipe.lastUpdated
                    }) temp = true
            }
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