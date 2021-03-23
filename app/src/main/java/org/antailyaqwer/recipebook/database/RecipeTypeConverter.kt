package org.antailyaqwer.recipebook.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class RecipeTypeConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? =
        uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? =
        UUID.fromString(uuid) ?: null

    @TypeConverter
    fun fromImages(images: List<String>): String? =
        Gson().toJson(images)

    @TypeConverter
    fun toImages(images: String?): List<String> =
        Gson().fromJson(images, Array<String>::class.java).toList()
}