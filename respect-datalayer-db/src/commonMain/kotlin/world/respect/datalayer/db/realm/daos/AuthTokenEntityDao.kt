package world.respect.datalayer.db.realm.daos

import androidx.room.Dao
import androidx.room.Insert
import world.respect.datalayer.db.realm.entities.AuthTokenEntity

@Dao
interface AuthTokenEntityDao {

    @Insert
    suspend fun insert(token: AuthTokenEntity)

}