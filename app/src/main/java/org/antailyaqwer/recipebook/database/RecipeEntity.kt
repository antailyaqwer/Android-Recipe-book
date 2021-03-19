package org.antailyaqwer.recipebook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RecipeEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val images: List<String>,
    var lastUpdated: Date,
    val description: String,
    val instructions: String,
    val difficulty: Int
)