package org.antailyaqwer.recipebook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class RecipeEntity(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val images: List<String>,
//    @Expose var lastUpdated: Date,
//    var lastUpdated: Date,
    var lastUpdated: Int,
    val description: String,
    val instructions: String,
    val difficulty: Int
)