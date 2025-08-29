package world.respect.datalayer.db.school.adapters

import world.respect.datalayer.db.school.entities.AuthTokenEntity
import world.respect.datalayer.school.model.AuthToken
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
