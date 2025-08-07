package world.respect.datalayer.db.realmdirectory.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import world.respect.datalayer.db.realmdirectory.entities.RealmConfigEntity

@Dao
interface RealmConfigEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(realmConfigEntity: RealmConfigEntity)

    @Query("""
        SELECT RealmConfigEntity.*
          FROM RealmConfigEntity
         WHERE RealmConfigEntity.rcUid = :uid 
    """)
    suspend fun findByUid(uid: Long): RealmConfigEntity?

}