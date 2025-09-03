package world.respect.datalayer.repository

import kotlinx.coroutines.runBlocking
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.school.PersonDataSource
import kotlin.test.Test

/**
 *
 */
class ExtendedValidationHelperIntegrationTest {

    class ExtendedValidationTestContext(
        val port: Int,
    )

    private fun extendedValidationIntegrationTest(
        block: ExtendedValidationTestContext.() -> Unit
    ) {

    }

    /**
     * Test that once a request is made, the next request will use the since parameter.
     */
    @Test
    fun givenPreviousRequestMade_whenAnotherRequestSent_thenSinceParamSetUsingConsistentThrough() {
        lateinit var personDataSource: PersonDataSource

        runBlocking {
            val answer1 = personDataSource.findAll(DataLoadParams(), null)
            //answer1 remote state should include the person

            val answer2 = personDataSource.findAll(DataLoadParams(), null)
            //answer2 remote state should not include the person

            //now change the person info

            val answer3 = personDataSource.findAll(DataLoadParams(), null)
        }
    }


}