package world.respect.datasource.db.opds.daos

import androidx.room.Dao
import androidx.room.Query
import world.respect.datasource.db.opds.OpdsParentType
import world.respect.datasource.db.opds.daos.OpdsPublicationEntityDao.Companion.PUBLICATION_UIDS_FOR_FEED_UID_CTE
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity

@Dao
abstract class ReadiumLinkEntityDao {

    @Query("""
         WITH $PUBLICATION_UIDS_FOR_FEED_UID_CTE 

       SELECT ReadiumLinkEntity.*
         FROM ReadiumLinkEntity
        WHERE (     ReadiumLinkEntity.rleOpdsParentType = ${OpdsParentType.ID_FEED}
                AND ReadiumLinkEntity.rleOpdsParentUid = :feedUid)
          OR  (     ReadiumLinkEntity.rleOpdsParentType = ${OpdsParentType.ID_PUBLICATION}
                AND ReadiumLinkEntity.rleOpdsParentUid IN (SELECT publicationUid FROM FeedPublicationUids))
    """)
    abstract suspend fun findAllByFeedUid(feedUid: Long): List<ReadiumLinkEntity>

}