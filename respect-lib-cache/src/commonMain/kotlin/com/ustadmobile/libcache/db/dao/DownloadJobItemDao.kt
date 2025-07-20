package com.ustadmobile.libcache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.ustadmobile.libcache.db.entities.DownloadJobItem

@Dao
abstract class DownloadJobItemDao {

    @Insert
    abstract suspend fun insertList(items: List<DownloadJobItem>)

}