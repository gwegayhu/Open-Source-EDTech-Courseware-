package world.respect.datalayer.repository.school

import androidx.paging.PagingSource
import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.repository.clientservertest.clientServerDatasourceTest
import world.respect.datalayer.school.model.Person
import world.respect.libutil.util.time.systemTimeInMillis
import world.respect.server.routes.school.respect.PersonRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

/**
 * This integration test checks if using consistent-through and since parameters works with a real
 * datasource as expected.
 */
class PersonRepositoryIntegrationTest {

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    val defaultTestPerson = Person(
        guid = "42",
        username = "test",
        givenName = "test",
        familyName = "test",
        roles = emptyList(),
    )

    @Test
    fun givenRequestMade_whenSameRequestMadeAgain_thenRemoteDataWillReturnNotModified() {
        runBlocking {
            clientServerDatasourceTest(temporaryFolder.newFolder("test")) {
                serverRouting {
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { serverSchoolDataSource })
                    }
                }

                server.start()

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson)
                )

                val initData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)

                val validatedData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)

                assertEquals(
                    defaultTestPerson.guid,
                    initData.dataOrNull()!!.first().guid
                )

                assertEquals(
                    NoDataLoadedState.Reason.NOT_MODIFIED,
                    (validatedData.remoteState as? NoDataLoadedState)?.reason
                )

                assertEquals(
                    defaultTestPerson.guid,
                    validatedData.dataOrNull()!!.first().guid
                )
            }
        }
    }

    @Test
    fun givenRequestMade_whenDataChangedAndSameRequestMadeAgain_thenRemoteDataWillBeLoaded() {
        runBlocking {
            clientServerDatasourceTest(temporaryFolder.newFolder("test")) {
                serverRouting {
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { serverSchoolDataSource })
                    }
                }

                server.start()

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson)
                )

                val initData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)

                val updatedName = "updated"
                //DataSource will need to reject same-second changes and respond with a wait message.
                Thread.sleep(2_000)

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson.copy(givenName = updatedName))
                )

                val newData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)

                assertEquals(
                    defaultTestPerson.guid,
                    initData.dataOrNull()!!.first().guid
                )

                assertTrue(newData.remoteState is DataReadyState)

                assertEquals(
                    updatedName,
                    newData.dataOrNull()!!.first().givenName
                )
            }
        }
    }


    /**
     * Test that once a request is made, the next request will use the since parameter.
     */
    @Test
    fun givenRequestMade_whenNextRequestSinceParamSetToPreviousConsistentThroughValue_thenRemoteResultShouldBeEmpty() {
        runBlocking {
            clientServerDatasourceTest(temporaryFolder.newFolder("test")) {
                serverRouting {
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { serverSchoolDataSource })
                    }
                }

                server.start()

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson)
                )

                val startTime = systemTimeInMillis()
                val initData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)
                println(initData)
                val answer1ConsistentThrough = initData.remoteState?.metaInfo?.consistentThrough!!
                assertTrue(initData.remoteState?.metaInfo?.consistentThrough!! >= startTime)

                val dataSince = clients.first().schoolDataSource.personDataSource
                    .findAll(
                        loadParams = DataLoadParams(),
                        since = Instant.Companion.fromEpochMilliseconds(answer1ConsistentThrough)
                    )

                val remoteDataState = dataSince.remoteState
                assertTrue(remoteDataState is DataReadyState)
                val remoteData = remoteDataState.data as List<*>
                assertEquals(0, remoteData.size)

                println("Run time: ${systemTimeInMillis() - startTime}")
            }
        }
    }

    @Test
    fun givenRequestMade_whenDataChangedAndNextRequestSinceParamSetToPreviousConsistentThroughValue_thenRemoteResultShouldBeUpdated() {
        runBlocking {
            clientServerDatasourceTest(temporaryFolder.newFolder("test")) {
                serverRouting {
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { serverSchoolDataSource })
                    }
                }

                server.start()

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson)
                )

                val startTime = systemTimeInMillis()
                val initData = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)
                println(initData)
                val answer1ConsistentThrough = initData.remoteState?.metaInfo?.consistentThrough!!
                assertTrue(initData.remoteState?.metaInfo?.consistentThrough!! >= startTime)

                val updatedName = "updated"
                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson.copy(givenName = "updated"))
                )

                val dataSince = clients.first().schoolDataSource.personDataSource
                    .findAll(
                        loadParams = DataLoadParams(),
                        since = Instant.Companion.fromEpochMilliseconds(answer1ConsistentThrough)
                    )

                val remoteDataState = dataSince.remoteState
                assertTrue(remoteDataState is DataReadyState)
                @Suppress("UNCHECKED_CAST")
                val remoteData = remoteDataState.data as List<Person>
                assertEquals(1, remoteData.size)
                assertEquals(updatedName, remoteData.first().givenName)

                println("Run time: ${systemTimeInMillis() - startTime}")
            }
        }
    }

    @Test
    fun givenRemotePagingSourceWillLoad() {
        runBlocking {
            clientServerDatasourceTest(temporaryFolder.newFolder("test")) {
                serverRouting {
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { serverSchoolDataSource })
                    }
                }

                server.start()

                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(defaultTestPerson)
                )

                val pagingSource = clients.first().schoolDataSource.personDataSource.findAllAsPagingSource(
                    loadParams = DataLoadParams(),
                )
                val data = pagingSource.load(
                    PagingSource.LoadParams.Refresh(0, 50, false)
                )
                println(data)
            }
        }
    }


}