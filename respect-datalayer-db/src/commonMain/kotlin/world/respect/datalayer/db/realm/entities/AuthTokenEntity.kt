package world.respect.datalayer.db.realm.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @property atPGuidHash foreign key for the related PersonEntity.pGuidHash
 * @property atToken the token itself
 * @property atTimeCreated time created (in millis)
 * @property atTtl ttl (in seconds)
 */
@Entity
data class AuthTokenEntity(
    @PrimaryKey(autoGenerate = true)
    val atUid: Long,
    val atPGuidHash: Long,
    val atCode: String?,
    val atToken: String,
    val atTimeCreated: Long,
    val atTtl: Int,
)
