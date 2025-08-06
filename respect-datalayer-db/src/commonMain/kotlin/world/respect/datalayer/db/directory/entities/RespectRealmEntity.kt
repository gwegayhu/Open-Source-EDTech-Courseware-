package world.respect.datalayer.db.directory.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.http.Url

/**
 * @property rrUid the XXHash64 of the rrSelfUrl
 */
@Entity
data class RespectRealmEntity(
    @PrimaryKey
    val rrUid: Long,
    val name: String,
    val rrSelf: Url,
    val rrXapi: Url,
    val rrOneRoster: Url,
    val rrRespectExt: Url?,
)