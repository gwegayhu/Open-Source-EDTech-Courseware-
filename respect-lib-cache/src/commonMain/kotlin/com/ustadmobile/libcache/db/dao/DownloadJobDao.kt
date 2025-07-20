package com.ustadmobile.libcache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ustadmobile.libcache.db.entities.DownloadJob

@Dao
abstract class DownloadJobDao {

    @Insert
    abstract suspend fun insert(downloadJob: DownloadJob): Long

    @Query(
        """
        SELECT * 
          FROM DownloadJob 
         WHERE tjUid = :uid
    """
    )
    abstract suspend fun findByUid(uid: Int): DownloadJob?

}