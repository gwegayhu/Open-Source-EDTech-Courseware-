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
         WHERE LangMapEntity.lmeTopParentType = :lmeTableId
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract suspend fun deleteByTableAndEntityUid(
        lmeTableId: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long = 0,
    )

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTableId
    """
    )
    abstract fun selectAllByTableId(
        lmeTableId: Int,
    ): Flow<List<LangMapEntity>>

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTableId
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract suspend fun selectAllByTableAndEntityId(
        lmeTableId: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long,
    ): List<LangMapEntity>

    @Query(
        """
        SELECT * 
          FROM LangMapEntity
         WHERE LangMapEntity.lmeTopParentType = :lmeTableId
           AND LangMapEntity.lmeTopParentUid1 = :lmeEntityUid1
           AND LangMapEntity.lmeTopParentUid2 = :lmeEntityUid2
    """
    )
    abstract fun selectAllByTableAndEntityIdAsFlow(
        lmeTableId: Int,
        lmeEntityUid1: Long,
        lmeEntityUid2: Long,
    ): Flow<List<LangMapEntity>>

}