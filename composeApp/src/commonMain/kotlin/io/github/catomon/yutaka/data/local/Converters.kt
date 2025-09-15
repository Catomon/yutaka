package io.github.catomon.yutaka.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class StringListConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(data: String): List<String> = Json.decodeFromString(data)
}