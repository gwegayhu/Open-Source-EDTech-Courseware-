package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.http.Url

@Entity
class OpdsFeedEntity(
    @PrimaryKey
    val ofeUid: Long,
    val ofeUrl: Url,
    val ofeUrlHash: Long,
    val ofeLastModifiedHeader: Long,
) {

    companion object {

        const val TABLE_ID = 27
    }

}