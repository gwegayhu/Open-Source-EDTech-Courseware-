package world.respect.datalayer.realm

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.realm.model.Person

interface PersonDataSource {

    suspend fun findByUsername(username: String): Person?

    suspend fun findByGuid(guid: String): Person?

    suspend fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>>

    suspend fun addPerson(person: Person)

}