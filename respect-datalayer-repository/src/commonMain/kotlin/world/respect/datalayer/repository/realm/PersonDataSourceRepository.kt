package world.respect.datalayer.repository.realm

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.realm.PersonDataSource
import world.respect.datalayer.realm.PersonDataSourceLocal
import world.respect.datalayer.realm.model.Person

class PersonDataSourceRepository(
    private val local: PersonDataSourceLocal,
    private val remote: PersonDataSource,
) : PersonDataSource {

    override suspend fun findByUsername(username: String): Person? {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPerson(person: Person) {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuid(guid: String): Person? {
        TODO("Not yet implemented")
    }
}