package world.respect.datalayer.db.realmdirectory.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.http.Url

/**
 * @property rdUid the xxhash64 of the rdUrl
 * @property rdUrl the directory base url
 * @param rdInvitePrefix
 */
@Entity
data class RealmDirectoryEntity(
    @PrimaryKey
    val rdUid: Long,
    val rdUrl: Url,
    val rdInvitePrefix: String,
)
