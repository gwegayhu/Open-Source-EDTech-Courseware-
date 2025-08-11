package world.respect.datalayer.db.realm.adapters

import world.respect.datalayer.db.realm.entities.AuthTokenEntity
import world.respect.datalayer.realm.model.AuthToken
import world.respect.libutil.ext.randomString

fun AuthToken.toEntity(
    pGuid: String,
    pGuidHash: Long,
    code: String = randomString(32)
): AuthTokenEntity {
    return AuthTokenEntity(
        atPGuidHash = pGuidHash,
        atPGuid = pGuid,
        atCode = code,
        atToken = accessToken,
        atTimeCreated = timeCreated,
        atTtl =  ttl
    )
}

fun AuthTokenEntity.toModel() : AuthToken {
    return AuthToken(
        accessToken = atToken,
        timeCreated = atTimeCreated,
        ttl = atTtl
    )
}
