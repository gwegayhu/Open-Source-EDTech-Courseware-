package world.respect.datalayer.db.realmdirectory.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import world.respect.datalayer.db.realmdirectory.entities.RealmEntity

@Dao
interface RealmEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(realmEntity: RealmEntity)

    @Query("""
        SELECT * 
         FROM RealmEntity
        WHERE reUid = :uid
    """)
    suspend fun findByUid(uid: Long): RealmEntity?

}