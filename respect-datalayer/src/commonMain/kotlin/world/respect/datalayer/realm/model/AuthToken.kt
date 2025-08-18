package world.respect.datalayer.realm.model

import kotlinx.serialization.Serializable

@Serializable
class AuthToken(
    val accessToken: String,
    val timeCreated: Long,
    val ttl: Int,
)