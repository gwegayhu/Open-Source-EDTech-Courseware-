package world.respect.datalayer.db.realm.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import world.respect.datalayer.db.realm.entities.PersonEntity

@Dao
interface PersonEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personEntity: PersonEntity)

}