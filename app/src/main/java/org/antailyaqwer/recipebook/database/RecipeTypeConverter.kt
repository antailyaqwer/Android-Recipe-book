package org.antailyaqwer.recipebook.database

import androidx.room.TypeConverter
import java.util.*

class RecipeTypeConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? =
        uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? =
        UUID.fromString(uuid) ?: null

    @TypeConverter
    fun fromDate(date: Date?): Int? {
        val tempDate = date?.time
        // Для совместимости Long и Int в миллисекундах
        return tempDate?.toInt()?.div(1000)
    }

    @TypeConverter
    fun toDate(date: Int?): Date? {
        var tempDate: Long? = null
        // Для совместимости Long и Int в миллисекундах
        if (date != null) {
            tempDate = date.toLong()
            tempDate *= 1000
        }
        return tempDate?.let {
            Date(it)
        }
    }
}