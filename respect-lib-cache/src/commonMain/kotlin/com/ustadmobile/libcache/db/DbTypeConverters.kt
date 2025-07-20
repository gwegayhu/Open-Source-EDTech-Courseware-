package com.ustadmobile.libcache.db

import androidx.room.TypeConverter
import io.ktor.http.Url

class DbTypeConverters {

    @TypeConverter
    fun fromUrl(value: Url?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toUrl(value: String?): Url? {
        return value?.let { Url(it) }
    }

}