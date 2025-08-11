package world.respect.datalayer.db.realm.adapters

import world.respect.datalayer.db.realm.entities.PersonEntity
import world.respect.datalayer.realm.model.Person
import world.respect.libxxhash.XXStringHasher
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class PersonEntities(
    val personEntity: PersonEntity
)

@OptIn(ExperimentalTime::class)
fun PersonEntities.toModel(): Person {
    return Person(
        guid = personEntity.pGuid,
        active = personEntity.pActive,
        lastModified = Instant.fromEpochMilliseconds(personEntity.pLastModified),
        username = personEntity.pUsername,
        givenName = personEntity.pGivenName,
        familyName = personEntity.pFamilyName,
        middleName = personEntity.pMiddleName
    )
}

@OptIn(ExperimentalTime::class)
fun Person.toEntities(
    xxStringHasher: XXStringHasher
): PersonEntities {
    return PersonEntities(
        personEntity = PersonEntity(
            pGuid = guid,
            pGuidHash = xxStringHasher.hash(guid),
            pActive = active,
            pLastModified = lastModified.toEpochMilliseconds(),
            pUsername = username,
            pGivenName = givenName,
            pFamilyName = familyName,
            pMiddleName = middleName
        )
    )
}


