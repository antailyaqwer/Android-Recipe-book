package org.antailyaqwer.recipebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipeentity WHERE id=(:id)")
    fun getRecipe(id: UUID): LiveData<RecipeEntity?>

    @Query(
        "SELECT * FROM recipeentity ORDER BY " +
                "CASE WHEN :sortType = 1 THEN name END ASC,  " +
                "CASE WHEN :sortType = 0 THEN name END DESC"
    )
    fun getAllRecipesOrderedByName(sortType: Boolean = false): LiveData<List<RecipeEntity>>

    @Query(
        "SELECT * FROM recipeentity ORDER BY " +
                "CASE WHEN :sortType = 1 THEN lastUpdated END ASC,  " +
                "CASE WHEN :sortType = 0 THEN lastUpdated END DESC"
    )
    fun getAllRecipesOrderedByLastUpdated(sortType: Boolean = false): LiveData<List<RecipeEntity>>

    @Update
    fun updateRecipe(recipe: RecipeEntity)

    @Insert
    fun addRecipe(recipe: RecipeEntity)
}