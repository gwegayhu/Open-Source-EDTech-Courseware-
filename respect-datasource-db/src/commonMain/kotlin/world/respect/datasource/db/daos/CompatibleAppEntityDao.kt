package world.respect.datasource.db.daos

import androidx.room.Dao
import androidx.room.Query
import world.respect.datasource.db.entities.CompatibleAppEntity

@Dao
interface CompatibleAppEntityDao {

    @Query("SELECT * FROM CompatibleAppEntity")
    suspend fun selectAll(): List<CompatibleAppEntity>

}