package world.respect.datalayer.db.school

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.db.RespectSchoolDatabase
import world.respect.datalayer.db.school.adapters.PersonEntities
import world.respect.datalayer.db.school.adapters.toEntities
import world.respect.datalayer.db.school.adapters.toModel
import world.respect.datalayer.school.PersonDataSourceLocal
import world.respect.datalayer.school.model.Person
import world.respect.datalayer.school.model.composites.PersonListDetails
import world.respect.libxxhash.XXStringHasher

class PersonDataSourceDb(
    private val schoolDb: RespectSchoolDatabase,
    private val xxHash: XXStringHasher,
): PersonDataSourceLocal {

    override suspend fun getAllUsers(): List<Person> {

        return schoolDb.getPersonEntityDao().getAllUsers().map {
            PersonEntities(it).toModel()
        }
    }

    override suspend fun findByUsername(username: String): Person? {
        return schoolDb.getPersonEntityDao().findByUsername(username)?.let {
            PersonEntities(it)
        }?.toModel()
    }

    override suspend fun findByGuid(
        loadParams: DataLoadParams,
        guid: String
    ): DataLoadState<Person> {
        return schoolDb.getPersonEntityDao().findByGuidHash(xxHash.hash(guid))?.let {
            PersonEntities(it)
        }?.toModel()?.let { DataReadyState(it) } ?: NoDataLoadedState.notFound()
    }

    override suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        return schoolDb.getPersonEntityDao().findByGuidHashAsFlow(
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

        schoolDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                schoolDb.getPersonEntityDao().insert(entities.personEntity)
                schoolDb.getPersonRoleEntityDao().deleteByPersonGuidHash(
                    entities.personEntity.pGuidHash
                )
                schoolDb.getPersonRoleEntityDao().upsertList(entities.personRoleEntities)
            }
        }
    }

    override suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<PersonListDetails>>> {
        return schoolDb.getPersonEntityDao().findAllListDetailsAsFlow().map {
            DataReadyState(it)
        }
    }

}