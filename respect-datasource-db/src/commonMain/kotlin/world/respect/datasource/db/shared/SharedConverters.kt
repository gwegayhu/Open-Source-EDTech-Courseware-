package world.respect.datasource.db.shared

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class SharedConverters {

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun stringListToString(value: List<String>?): String? {
        return value?.let { Json.encodeToString(it) }
    }


}