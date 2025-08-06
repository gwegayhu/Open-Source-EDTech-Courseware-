package world.respect.datalayer.db.realmdirectory.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import world.respect.datalayer.db.realmdirectory.entities.RealmEntity

@Dao
interface RealmEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(realmEntity: RealmEntity)

}