package world.respect.datalayer.db.school.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import world.respect.datalayer.db.school.entities.PersonPasswordEntity

@Dao
interface PersonPasswordEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(personPasswordEntity: PersonPasswordEntity)

    @Query(
        """
            SELECT * 
             FROM PersonPasswordEntity
            WHERE pppGuid = :uid
        """
    )
    suspend fun findByUid(uid: Long): PersonPasswordEntity?

}