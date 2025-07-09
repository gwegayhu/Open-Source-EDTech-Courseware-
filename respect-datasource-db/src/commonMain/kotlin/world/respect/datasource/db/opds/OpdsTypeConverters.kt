package world.respect.datasource.db.opds

import androidx.room.TypeConverter

class OpdsTypeConverters {

    @TypeConverter
    fun fromOpdsTopParentType(value: OpdsTopParentType): Int {
        return value.id
    }

    @TypeConverter
    fun toOpdsTopParentType(value: Int): OpdsTopParentType {
        return OpdsTopParentType.entries.first { it.id == value }
    }



}