package world.respect.datalayer.db.oneroaster.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.db.oneroster.entities.OneRosterClassEntity

@Dao
interface OneRoasterClassEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oneRosterClassEntity: OneRosterClassEntity)

    @Query("SELECT * FROM OneRosterClassEntity")
    suspend fun getAllClasses(): List<OneRosterClassEntity>

    @Query(
        """
        SELECT * 
         FROM OneRosterClassEntity
        WHERE classSourcedId = :sourcedId
    """
    )
    suspend fun findClassBySourcedId(sourcedId: String): OneRosterClassEntity?

    @Query(
        """
        SELECT * 
         FROM OneRosterClassEntity
        WHERE classSourcedId = :sourcedId
    """
    )
    fun findClassBySourcedIdAsFlow(sourcedId: String): Flow<OneRosterClassEntity?>

}
