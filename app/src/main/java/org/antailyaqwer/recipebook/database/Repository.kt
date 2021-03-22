package org.antailyaqwer.recipebook.database

import android.content.Context
import androidx.lifecycle.*
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

    fun getAllRecipesOrderedByNameAscending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByNameAscending()

    private fun getAllRecipesList(): List<RecipeEntity> =
        recipeDao.getAllRecipesList()

    fun getAllRecipesOrderedByDateAscending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByDateAscending()

    fun searchByNameOrDescriptionOrInstructions(query: String): LiveData<List<RecipeEntity>> =
        recipeDao.searchByNameOrDescriptionOrInstructions(
            StringBuilder().append(query, "%").toString()
        )

    fun updateRecipe(recipe: RecipeEntity) {
        executor.execute {
            recipeDao.updateRecipe(recipe)
        }
    }

    fun addRecipe(recipe: RecipeEntity) {
//        executor.execute {
        recipeDao.addRecipe(recipe)
//        }
    }

    fun hasRecipe(uuid: UUID): Boolean {
//        executor.execute {
        //TODO Создал специальный метод для получения в List
        for (element in getAllRecipesList()) {
            if (element.uuid == uuid) {
                return true
            }
        }
//        }
        return false
    }

    fun needUpdate(recipe: RecipeEntity): Boolean {
        var temp = false
        executor.execute {
            //TODO заменить на Transformations.map
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