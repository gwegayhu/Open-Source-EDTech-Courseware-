package world.respect.datalayer.http.school

import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.AuthTokenProvider
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.ext.firstOrNotLoaded
import world.respect.datalayer.ext.getAsDataLoadState
import world.respect.datalayer.ext.getDataLoadResultAsFlow
import world.respect.datalayer.ext.map
import world.respect.datalayer.http.ext.resolveRespectExtUrl
import world.respect.datalayer.school.PersonDataSource
import world.respect.datalayer.school.adapters.asListDetails
import world.respect.datalayer.school.model.Person
import world.respect.datalayer.school.model.composites.PersonListDetails
import world.respect.datalayer.schooldirectory.SchoolDirectoryDataSource
import world.respect.libutil.ext.resolve

class PersonDataSourceHttp(
    override val schoolUrl: Url,
    override val schoolDirectoryDataSource: SchoolDirectoryDataSource,
    private val httpClient: HttpClient,
    private val tokenProvider: AuthTokenProvider,
) : PersonDataSource, SchoolUrlBasedDataSource {

    override suspend fun findByUsername(username: String): Person? {
        TODO("Not yet implemented")
    }

    override suspend fun findByGuid(
        loadParams: DataLoadParams,
        guid: String
    ): DataLoadState<Person> {
        return httpClient.getAsDataLoadState<List<Person>>(
            resolveRespectExtUrl("person"),
        ) {
            headers[HttpHeaders.Authorization] = "Bearer ${tokenProvider.provideToken().accessToken}"
        }.firstOrNotLoaded()
    }

    override fun findByGuidAsFlow(guid: String): Flow<DataLoadState<Person>> {
        return httpClient.getDataLoadResultAsFlow<List<Person>>(
            urlFn = { resolveRespectExtUrl("person") },
            dataLoadParams = DataLoadParams()
        ) {
            headers[HttpHeaders.Authorization] = "Bearer ${tokenProvider.provideToken().accessToken}"
        }.map {
            it.firstOrNotLoaded()
        }
    }

    override fun findAllAsFlow(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<Person>>> {
        return httpClient.getDataLoadResultAsFlow<List<Person>>(
            urlFn = { resolveRespectExtUrl("person") },
            dataLoadParams = loadParams,
        ) {
            headers[HttpHeaders.Authorization] = "Bearer ${tokenProvider.provideToken().accessToken}"
        }
    }

    override fun findAllListDetailsAsFlow(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<PersonListDetails>>> {
        return findAllAsFlow(loadParams, searchQuery).map { dataLoadState ->
            dataLoadState.map { list ->
                list.map { it.asListDetails() }
            }
        }
    }

    override suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): DataLoadState<List<Person>> {
        val respectUrl = schoolDirectoryDataSource.getSchoolDirectoryEntryByUrl(schoolUrl).dataOrNull()
            ?.respectExt ?: throw IllegalStateException("No Respect Ext url")

        return httpClient.getAsDataLoadState<List<Person>>(respectUrl.resolve("person")) {
            val token = tokenProvider.provideToken()
            println("PersonDataSource: load person list using token $token")
            headers[HttpHeaders.Authorization] = "Bearer ${token.accessToken}"
        }
    }
}