package world.respect.datasource.db.opds.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import world.respect.datasource.db.opds.entities.OpdsFeedEntity

@Dao
abstract class OpdsFeedEntityDao {

    @Query("""
        SELECT * 
          FROM OpdsFeedEntity 
         WHERE ofeUrlHash = :urlHash
    """)
    abstract fun findByUrlHashAsFlow(urlHash: Long): Flow<OpdsFeedEntity?>

}