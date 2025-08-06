package world.respect.datalayer.db.realm.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    val pGuid: String,
    @PrimaryKey
    val pGuidHash: Long,
    val pActive: Boolean,
    val pLastModified: Long,
    val pUsername: String? = null,
    val pGivenName: String,
    val pFamilyName: String,
    val pMiddleName: String? = null,
)