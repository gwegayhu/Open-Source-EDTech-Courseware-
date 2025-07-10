package world.respect.datasource.db.opds.daos

import androidx.room.Dao
import androidx.room.Query
import world.respect.datasource.db.opds.entities.OpdsGroupEntity

@Dao
abstract class OpdsGroupEntityDao {

    @Query("""
        SELECT OpdsGroupEntity.*
          FROM OpdsGroupEntity
         WHERE OpdsGroupEntity.ogeOfeUid = :feedUid
    """)
    abstract suspend fun findByFeedUid(feedUid: Long): List<OpdsGroupEntity>
}