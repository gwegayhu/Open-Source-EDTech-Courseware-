package world.respect.datalayer.repository.school

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.ext.combineWithRemote
import world.respect.datalayer.school.PersonDataSource
import world.respect.datalayer.school.PersonDataSourceLocal
import world.respect.datalayer.school.model.Person
import world.respect.datalayer.school.model.composites.PersonListDetails

class PersonDataSourceRepository(
    private val local: PersonDataSourceLocal,
    private val remote: PersonDataSource,
) : PersonDataSource {

    override suspend fun findByUsername(username: String): Person? {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuid(
        loadParams: DataLoadParams,
        guid: String
    ): DataLoadState<Person> {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        TODO("Not yet implemented")
    }

    override fun findAllListDetailsAsFlow(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<PersonListDetails>>> {
        val remoteFlow = remote.findAllAsFlow(loadParams, searchQuery).onEach {
            if(it is DataReadyState) {
                local.putPersonsLocal(it.data)
            }
        }

        val localFeed = local.findAllListDetailsAsFlow(loadParams, searchQuery)
        return localFeed.combineWithRemote(remoteFlow)
    }

    override fun findAllAsFlow(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<Person>>> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): DataLoadState<List<Person>> {
        TODO("Not yet implemented")
    }
}