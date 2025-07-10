package world.respect.datasource.db.opds.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
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

    @Query("""
        DELETE FROM OpdsPublicationEntity 
         WHERE opeUid = :pubUid
    """)
    abstract suspend fun deleteByUid(pubUid: Long)

    @Insert
    abstract suspend fun insertList(entities: List<OpdsPublicationEntity>)

    @Query("""
        SELECT OpdsPublicationEntity.opeUid
          FROM OpdsPublicationEntity
         WHERE OpdsPublicationEntity.opeUrlHash = :urlHash
    """)
    abstract suspend fun getUidByUrlHash(urlHash: Long): Long

    @Query("""
        SELECT OpdsPublicationEntity.*
          FROM OpdsPublicationEntity
         WHERE OpdsPublicationEntity.opeUrlHash = :urlHash
    """)
    abstract fun findByUrlHashAsFlow(urlHash: Long): Flow<OpdsPublicationEntity?>

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