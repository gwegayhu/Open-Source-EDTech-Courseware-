package world.respect.datalayer.db.realm

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
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

    override suspend fun findByGuid(guid: String): Person? {
        return realmDb.getPersonEntityDao().findByGuidHash(xxHash.hash(guid))?.let {
            PersonEntities(it)
        }?.toModel()
    }

    override suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        return realmDb.getPersonEntityDao().findByGuidHashAsFlow(
            xxHash.hash(guid)
        ).map { personEntity ->
            if(personEntity != null) {
                DataReadyState(
                    data = PersonEntities(personEntity).toModel()
                )
            } else {
                NoDataLoadedState(NoDataLoadedState.Reason.NOT_FOUND)
            }
        }
    }

    override suspend fun putPerson(person: Person) {
        val entities = person.toEntities(xxHash)

        realmDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                realmDb.getPersonEntityDao().insert(entities.personEntity)
                realmDb.getPersonRoleEntityDao().deleteByPersonGuidHash(
                    entities.personEntity.pGuidHash
                )
                realmDb.getPersonRoleEntityDao().upsertList(entities.personRoleEntities)
            }
        }
    }
}