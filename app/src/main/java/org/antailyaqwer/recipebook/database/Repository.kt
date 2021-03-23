package org.antailyaqwer.recipebook.database

import android.content.Context
import androidx.lifecycle.*
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "recipe-database"

class Repository private constructor(context: Context) {

    private val database: RecipeDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecipeDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val recipeDao = database.recipeDao()

    fun getRecipe(id: UUID): LiveData<RecipeEntity?> = recipeDao.getRecipe(id)

    fun getAllRecipesOrderedByNameAscending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByNameAscending()

    fun getAllRecipesOrderedByNameDescending(): LiveData<List<RecipeEntity>> =
        recipeDao.getAllRecipesOrderedByNameDescending()

    private fun getAllRecipesList(): List<RecipeEntity> =
        recipeDao.getAllRecipesList()

    fun searchByNameOrDescriptionOrInstructions(query: String): LiveData<List<RecipeEntity>> =
        recipeDao.searchByNameOrDescriptionOrInstructions(
            StringBuilder().append(query, "%").toString()
        )

    fun addRecipe(recipe: RecipeEntity) =
        recipeDao.addRecipe(recipe)

    fun hasRecipe(uuid: UUID): Boolean {
        for (element in getAllRecipesList()) {
            if (element.uuid == uuid) {
                return true
            }
        }
        return false
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