package world.respect.datalayer.db.opds.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.db.opds.entities.OpdsFeedEntity

@Dao
abstract class OpdsFeedEntityDao {

    @Query("""
        SELECT * 
          FROM OpdsFeedEntity 
         WHERE ofeUrlHash = :urlHash
    """)
    abstract fun findByUrlHashAsFlow(urlHash: Long): Flow<OpdsFeedEntity?>

    @Query("""
        DELETE FROM OpdsFeedEntity 
         WHERE ofeUid = :feedUid
    """)
    abstract suspend fun deleteByFeedUid(feedUid: Long)

    @Insert
    abstract suspend fun insertList(entities: List<OpdsFeedEntity>)

}