package world.respect.datalayer.db.schooldirectory.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.db.schooldirectory.entities.SchoolDirectoryEntryEntity

@Dao
interface SchoolDirectoryEntryEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(realmEntity: SchoolDirectoryEntryEntity)

    @Query(
        """
        SELECT * 
         FROM SchoolDirectoryEntryEntity
        WHERE reUid = :uid
    """
    )
    suspend fun findByUid(uid: Long): SchoolDirectoryEntryEntity?

    @Query(
        """
         SELECT SchoolDirectoryEntryEntity.*
          FROM SchoolDirectoryEntryEntity
         JOIN LangMapEntity
         ON LangMapEntity.lmeTopParentUid1 = SchoolDirectoryEntryEntity.reUid
         WHERE LangMapEntity.lmeValue LIKE :query
     """
    )
    fun searchSchoolsByName(query: String): Flow<List<SchoolDirectoryEntryEntity>>



}