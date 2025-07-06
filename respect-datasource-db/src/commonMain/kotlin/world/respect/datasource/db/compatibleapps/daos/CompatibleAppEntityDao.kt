package world.respect.datasource.db.compatibleapps.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datasource.db.compatibleapps.entities.CompatibleAppEntity

@Dao
interface CompatibleAppEntityDao {

    @Query("SELECT * FROM CompatibleAppEntity")
    fun selectAllAsFlow(): Flow<List<CompatibleAppEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun upsert(compatibleApps: List<CompatibleAppEntity>)

}