package org.antailyaqwer.recipebook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class RecipeEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val images: ArrayList<String>,
    var lastUpdated: Date,
    val description: String,
    val instructions: String,
    val difficulty: Int
)