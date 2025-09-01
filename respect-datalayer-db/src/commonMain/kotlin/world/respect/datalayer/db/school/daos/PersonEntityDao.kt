package world.respect.datalayer.db.school.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.db.school.entities.PersonEntity
import world.respect.datalayer.school.model.composites.PersonListDetails

@Dao
interface PersonEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personEntity: PersonEntity)

    @Query("""
        SELECT * 
         FROM PersonEntity
        WHERE pUsername = :username
    """)
    suspend fun findByUsername(username: String): PersonEntity?

    @Query("""
       SELECT * 
         FROM PersonEntity
        WHERE pGuidHash = :guidHash
    """)
    suspend fun findByGuidHash(guidHash: Long): PersonEntity?

    @Query("""
        SELECT * 
         FROM PersonEntity
        WHERE pGuidHash = :guidHash
    """)
    fun findByGuidHashAsFlow(guidHash: Long): Flow<PersonEntity?>

    @Query("""
        SELECT PersonEntity.pGuid AS guid, 
               PersonEntity.pGivenName AS givenName, 
               PersonEntity.pFamilyName AS familyName, 
               PersonEntity.pUsername AS username
          FROM PersonEntity
    """)
    fun findAllListDetailsAsFlow(): Flow<List<PersonListDetails>>

    @Query("SELECT * FROM PersonEntity")
    suspend fun getAllUsers(): List<PersonEntity>

}