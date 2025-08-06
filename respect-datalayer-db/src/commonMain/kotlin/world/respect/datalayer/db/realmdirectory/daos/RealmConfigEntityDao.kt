package world.respect.datalayer.db.realmdirectory.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import world.respect.datalayer.db.realmdirectory.entities.RealmConfigEntity

@Dao
interface RealmConfigEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(realmConfigEntity: RealmConfigEntity)

}