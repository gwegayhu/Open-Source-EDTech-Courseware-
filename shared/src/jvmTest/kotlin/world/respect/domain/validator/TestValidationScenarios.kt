package world.respect.domain.validator

import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.kodein.di.DI
import org.kodein.di.instance
import world.respect.di.JvmCoreDiMOdule
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.respectdir.model.RespectAppManifest
import world.respect.testutil.copyResourcesToTempDir
import world.respect.testutil.findFreePort
import world.respect.testutil.recursiveFindAndReplace
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestValidationScenarios {

    @Rule
    @JvmField
    var tempFileRule = TemporaryFolder()

    data class ValidationScenarioContext(
        val reporter: ListAndPrintlnValidatorReporter,
        val testBaseUrl: String,
    )

    private fun testValidationScenario(
        caseName: String,
        caseResources: List<String> = listOf(
            "app.html", "appmanifest.json",  "index.json", "grade1/grade1.json",
            "grade1/lesson001/lesson001.html", "grade1/lesson001/lesson001.json"
        ),
        block: ValidationScenarioContext.() -> Unit,
    ) {
        val tempDir = tempFileRule.copyResourcesToTempDir(
            "/world/respect/validator/$caseName",
            resourceNames = caseResources,
        )

        val port = findFreePort()
        val testBaseUrl = "http://localhost:$port/resources"
        println("Test running on port $port")
        tempDir.recursiveFindAndReplace(
            fileFilter =  { it.extension in listOf("html", "json") },
            textReplacement = {
                it.replace("\$TESTBASEURL", testBaseUrl)
            }
        )

        val server = embeddedServer(Netty, port = port) {
            routing {
                staticFiles("/resources", tempDir)
            }
        }

        try {
            server.start()

            val di = DI {
                import(JvmCoreDiMOdule)
            }

            val validator: ValidateLinkUseCase by di.instance()

            val reporter = ListAndPrintlnValidatorReporter()
            runBlocking {
                validator(
                    link = ReadiumLink(
                        href = "http://localhost:$port/resources/appmanifest.json",
                        type = RespectAppManifest.MIME_TYPE,
                    ),
                    options = ValidateLinkUseCase.ValidatorOptions(
                        followLinks = true
                    ),
                    baseUrl = "http://localhost:$port/resources/appmanifest.json",
                    reporter = reporter,
                    visitedUrls = mutableListOf(),
                )
            }

            block(
                ValidationScenarioContext(
                    reporter = reporter,
                    testBaseUrl = testBaseUrl,
                )
            )
        } finally {
            server.stop()
        }
    }


    @Test
    fun givenValidManifest_whenValidated_thenWillReturnNoErrors() {
        testValidationScenario(
            caseName = "case_valid",
        ) {
            assertEquals(0, reporter.messages.count { it.level == ValidatorMessage.Level.ERROR })
        }
    }

    @Test
    fun givenManifestNotDiscoverable_whenValidated_thenWillReturnError() {
        testValidationScenario(
            caseName = "case_manifest_not_discoverable",
        ) {
            assertTrue(
                message = "Error message for manifest not being discoverable raised",
                actual = reporter.messages.any { it.message.contains("Manifest not discovered") }
            )
        }
    }

}