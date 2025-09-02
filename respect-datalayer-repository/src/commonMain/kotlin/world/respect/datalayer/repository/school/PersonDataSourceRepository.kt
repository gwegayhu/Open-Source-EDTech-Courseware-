package world.respect.datalayer.repository.school

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.school.PersonDataSource
import world.respect.datalayer.school.PersonDataSourceLocal
import world.respect.datalayer.school.model.Person
import world.respect.datalayer.school.model.composites.PersonListDetails

class PersonDataSourceRepository(
    private val local: PersonDataSourceLocal,
    private val remote: PersonDataSource,
) : PersonDataSource {

    override suspend fun getAllUsers(sourcedId: String): List<Person> {
        TODO("Not yet implemented")
    }

    override suspend fun findByUsername(username: String): Person? {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun putPerson(person: Person) {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuid(
        loadParams: DataLoadParams,
        guid: String
    ): DataLoadState<Person> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<PersonListDetails>>> {
        TODO("Not yet implemented")
    }
}