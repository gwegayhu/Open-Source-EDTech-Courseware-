package world.respect.datalayer.db.realm

import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.adapters.PersonEntities
import world.respect.datalayer.db.realm.adapters.toEntities
import world.respect.datalayer.db.realm.adapters.toModel
import world.respect.datalayer.realm.PersonDataSourceLocal
import world.respect.datalayer.realm.model.Person
import world.respect.libxxhash.XXStringHasher

class PersonDataSourceDb(
    private val realmDb: RespectRealmDatabase,
    private val xxHash: XXStringHasher,
): PersonDataSourceLocal {

    override suspend fun findByUsername(username: String): Person? {
        return realmDb.getPersonEntityDao().findByUsername(username)?.let {
            PersonEntities(it)
        }?.toModel()
    }

    override suspend fun addPerson(person: Person) {
        realmDb.getPersonEntityDao().insert(
            person.toEntities(xxHash).personEntity
        )
    }
}