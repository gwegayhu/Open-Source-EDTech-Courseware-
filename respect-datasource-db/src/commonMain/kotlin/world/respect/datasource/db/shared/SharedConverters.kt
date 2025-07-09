package world.respect.datasource.db.shared

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import world.respect.datasource.db.shared.entities.LangMapEntity

class SharedConverters {

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun fromLangMapTopParentType(value: LangMapEntity.TopParentType): Int {
        return value.id
    }

    @TypeConverter
    fun toLangMapTopParentType(value: Int): LangMapEntity.TopParentType {
        return LangMapEntity.TopParentType.entries.first { it.id == value }
    }

    @TypeConverter
    fun fromLangMapPropType(value: LangMapEntity.PropType): Int {
        return value.id
    }

    @TypeConverter
    fun toLangMapPropType(value: Int): LangMapEntity.PropType {
        return LangMapEntity.PropType.entries.first { it.id == value }
    }

}