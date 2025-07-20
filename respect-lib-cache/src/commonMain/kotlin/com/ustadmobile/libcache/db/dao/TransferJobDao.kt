package com.ustadmobile.libcache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.ustadmobile.libcache.db.entities.TransferJob

@Dao
abstract class TransferJobDao {

    @Insert
    abstract suspend fun insert(transferJob: TransferJob): Long

}