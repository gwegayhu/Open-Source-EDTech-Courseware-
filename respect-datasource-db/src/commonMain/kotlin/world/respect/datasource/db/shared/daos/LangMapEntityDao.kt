package world.respect.datasource.db.shared.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datasource.db.shared.entities.LangMapEntity

@Dao
abstract class LangMapEntityDao {

    @Insert
    abstract suspend fun insertAsync(entity: List<LangMapEntity>)

    @Query(
        """
        DELETE FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTopParentType
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract suspend fun deleteByTableAndTopParentType(
        lmeTopParentType: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long = 0,
    )

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTopParentType 
    """
    )
    abstract fun selectAllByTopParentType(
        lmeTopParentType: Int,
    ): Flow<List<LangMapEntity>>

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTopParentType
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract suspend fun selectAllByTableAndEntityId(
        lmeTopParentType: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long,
    ): List<LangMapEntity>

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTopParentTypeId
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract fun selectAllByTableAndEntityIdAsFlow(
        lmeTopParentTypeId: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long,
    ): Flow<List<LangMapEntity>>

}