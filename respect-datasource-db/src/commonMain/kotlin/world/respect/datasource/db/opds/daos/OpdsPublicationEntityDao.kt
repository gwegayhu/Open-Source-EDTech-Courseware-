package world.respect.datasource.db.opds.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import world.respect.datasource.db.opds.entities.OpdsPublicationEntity

@Dao
abstract class OpdsPublicationEntityDao {

    @Query("""
        SELECT OpdsPublicationEntity.*
          FROM OpdsPublicationEntity
         WHERE opeOfeUid = :feedUid 
    """)
    abstract suspend fun findByFeedUid(feedUid: Long): List<OpdsPublicationEntity>

    @Query("""
        DELETE FROM OpdsPublicationEntity 
         WHERE opeOfeUid = :feedUid
    """)
    abstract suspend fun deleteAllByFeedUid(feedUid: Long)

    @Insert
    abstract suspend fun insertList(entities: List<OpdsPublicationEntity>)

    companion object {

        const val PUBLICATION_UIDS_FOR_FEED_UID_CTE = """
            FeedPublicationUids(publicationUid) AS(
             SELECT OpdsPublicationEntity.opeUid
               FROM OpdsPublicationEntity
              WHERE OpdsPublicationEntity.opeOfeUid = :feedUid 
        )
        """

    }
}