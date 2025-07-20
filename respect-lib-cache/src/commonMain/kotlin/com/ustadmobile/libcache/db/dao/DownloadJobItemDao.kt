package com.ustadmobile.libcache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ustadmobile.libcache.db.entities.DownloadJobItem
import com.ustadmobile.libcache.db.entities.TransferJobItemStatus

@Dao
abstract class DownloadJobItemDao {

    @Insert
    abstract suspend fun insertList(items: List<DownloadJobItem>)

    @Query("""
        SELECT DownloadJobItem.*
          FROM DownloadJobItem
         WHERE DownloadJobItem.djiDjUid = :jobUid
           AND DownloadJobItem.djiStatus < ${TransferJobItemStatus.STATUS_COMPLETE_INT}
    """)
    abstract suspend fun findPendingByJobUid(jobUid: Int): List<DownloadJobItem>

    @Query("""
       SELECT COUNT(*)
         FROM DownloadJobItem
        WHERE DownloadJobItem.djiDjUid = :jobUid
          AND DownloadJobItem.djiStatus != ${TransferJobItemStatus.STATUS_COMPLETE_INT}
    """)
    abstract suspend fun findNumberJobItemsNotComplete(
        jobUid: Int,
    ): Int

    @Query("""
        UPDATE DownloadJobItem
           SET djiTransferred = :transferred
         WHERE djiUid = :jobItemUid
    """)
    abstract suspend fun updateTransferredProgress(
        jobItemUid: Int,
        transferred: Long,
    )


    @Query("""
        UPDATE DownloadJobItem
           SET djiStatus = :status
         WHERE djiUid = :jobItemUid  
    """)
    abstract suspend fun updateStatus(
        jobItemUid: Int,
        status: Int,
    )


}