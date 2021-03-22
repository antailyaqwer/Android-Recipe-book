package org.antailyaqwer.recipebook.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipeentity WHERE uuid=(:uuid)")
    fun getRecipe(uuid: UUID): LiveData<RecipeEntity?>

//    @Query(
//        "SELECT * FROM recipeentity ORDER BY " +
//                "CASE WHEN :sortType = 1 THEN name END ASC,  " +
//                "CASE WHEN :sortType = 0 THEN name END DESC"
//    )
//    fun getAllRecipesOrderedByName(sortType: Boolean = false): LiveData<List<RecipeEntity>>
//
//    @Query(
//        "SELECT * FROM recipeentity ORDER BY " +
//                "CASE WHEN :sortType = 1 THEN lastUpdated END ASC,  " +
//                "CASE WHEN :sortType = 0 THEN lastUpdated END DESC"
//    )
//    fun getAllRecipesOrderedByLastUpdated(sortType: Boolean = false): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity ORDER BY name ASC")
    fun getAllRecipesOrderedByNameAscending(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity ORDER BY name DESC")
    fun getAllRecipesOrderedByNameDescending(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity ORDER BY lastUpdated ASC")
    fun getAllRecipesOrderedByDateAscending(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity ORDER BY lastUpdated DESC")
    fun getAllRecipesOrderedByDateDescending(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity ORDER BY uuid")
    fun getAllRecipesList(): List<RecipeEntity>

    @Update
    fun updateRecipe(recipe: RecipeEntity)

    @Insert
    fun addRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipeentity WHERE name LIKE :query OR description LIKE :query OR instructions LIKE :query")
    fun searchByNameOrDescriptionOrInstructions(query: String): LiveData<List<RecipeEntity>>
}