package world.respect.datalayer.db.realm.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonPasswordEntity(
    @PrimaryKey
    val pppGuid: Long,
    val authAlgorithm: String,
    val authEncoded: String,
    val authSalt: String,
    val authIterations: Int,
    val authKeyLen: Int,
)
