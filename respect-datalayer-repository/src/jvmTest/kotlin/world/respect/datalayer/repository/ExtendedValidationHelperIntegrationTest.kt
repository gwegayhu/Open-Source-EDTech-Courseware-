package world.respect.datalayer.repository

import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.repository.clientservertest.clientServerDatasourceTest
import world.respect.datalayer.school.model.Person
import world.respect.libutil.util.time.systemTimeInMillis
import world.respect.server.routes.school.respect.PersonRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * This integration test checks if using consistent-through and since parameters works with a real
 * datasource as expected.
 */
class ExtendedValidationHelperIntegrationTest {

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    /**
     * Test that once a request is made, the next request will use the since parameter.
     */
    @Test
    fun givenPreviousRequestMade_whenAnotherRequestSent_thenSinceParamSetUsingConsistentThrough() {
        val personGuid = "42"

        runBlocking {
            clientServerDatasourceTest(
                temporaryFolder.newFolder("test"),
                serverRouting = { ctx ->
                    route("api/school/respect") {
                        PersonRoute(schoolDataSource = { ctx.serverSchoolDataSource } )
                    }
                }
            ) {
                val startTime = systemTimeInMillis()
                serverSchoolDataSource.personDataSource.putPersonsLocal(
                    listOf(
                        Person(
                            guid = personGuid,
                            username = "test",
                            givenName = "test",
                            familyName = "test",
                            roles = emptyList(),
                        )
                    )
                )

                val answer1 = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)
                println(answer1)
                assertTrue(answer1.remoteState?.metaInfo?.consistentThrough!! >= startTime)

                val answer2 = clients.first().schoolDataSource.personDataSource
                    .findAll(DataLoadParams(), null)
                assertEquals(NoDataLoadedState.Reason.NOT_MODIFIED,
                    (answer2.remoteState as? NoDataLoadedState)?.reason)


//            val answer1 = personDataSource.findAll(DataLoadParams(), null)
//            //answer1 remote state should include the person
//
//            val answer2 = personDataSource.findAll(DataLoadParams(), null)
//            //answer2 remote state should not include the person
//
//            //now change the person info
//            val answer3 = personDataSource.findAll(DataLoadParams(), null)
            }
        }
    }


}