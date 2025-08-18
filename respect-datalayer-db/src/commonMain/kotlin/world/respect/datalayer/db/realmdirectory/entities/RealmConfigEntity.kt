package world.respect.datalayer.db.realmdirectory.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the configuration of a realm which is hosted on this server.
 * @param rcUid UID as per RealmEntity.reUid
 * @param dbUrl database URL or path for the realm-specific database.
 */
@Entity
class RealmConfigEntity(
    @PrimaryKey
    val rcUid: Long,
    val dbUrl: String,
)
