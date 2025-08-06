package world.respect.datalayer.db.directory.entities

import androidx.room.Entity
import io.ktor.http.Url

/**
 * @property rdUid the xxhash64 of the rdUrl
 * @property rdUrl the directory base url
 * @param rdInvitePrefix
 */
@Entity
data class RespectDirectoryEntity(
    val rdUid: Long,
    val rdUrl: Url,
    val rdInvitePrefix: String,
)
