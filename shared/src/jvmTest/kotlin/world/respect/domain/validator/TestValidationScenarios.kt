package world.respect.domain.validator

import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.kodein.di.DI
import org.kodein.di.instance
import world.respect.di.JvmCoreDiMOdule
import world.respect.datasource.compatibleapps.model.RespectAppManifest
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
            "grade1/lesson001/lesson001.html", "grade1/lesson001/lesson001.json",
            "grade1/lesson001/audio.ogg", "grade1/lesson001/video.mp4",
            "grade1/lesson001/script.js", "grade1/lesson001/cover.png"
        ),
        installConditionalHeaders: Boolean = true,
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
            if(installConditionalHeaders) {
                install(ConditionalHeaders)
            }

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
                    link = world.respect.datasource.opds.model.ReadiumLink(
                        href = "http://localhost:$port/resources/appmanifest.json",
                        type = RespectAppManifest.MIME_TYPE,
                    ),
                    options = ValidateLinkUseCase.ValidatorOptions(
                        followLinks = true
                    ),
                    refererUrl = "http://localhost:$port/resources/appmanifest.json",
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
            val errors = reporter.messages.filter { it.level == ValidatorMessage.Level.ERROR }
            assertEquals(0, errors.size)
        }
    }

    @Test
    fun givenManifestNotDiscoverable_whenValidated_thenWillReturnError() {
        testValidationScenario(
            caseName = "case_manifest_not_discoverable",
        ) {
            assertTrue(
                message = "Reporter visited lesson",
                actual = reporter.messages.any {
                    it.sourceUri == "$testBaseUrl/grade1/lesson001/lesson001.json"
                }
            )
            assertTrue(
                message = "Error message for manifest not being discoverable raised",
                actual = reporter.messages.any {
                    it.level == ValidatorMessage.Level.ERROR &&
                        it.message.contains("Manifest not discovered")
                }
            )
        }
    }

    @Test
    fun givenManifestDoesNotListResources_whenValidated_thenWillReturnError() {
        testValidationScenario(
            caseName = "case_no_resources_in_manifest"
        ) {
            assertTrue(
                message = "Got error for manifest not listing resources",
                actual = reporter.messages.any {
                    it.level == ValidatorMessage.Level.ERROR &&
                        it.message.contains("The manifest which is discovered using the") &&
                        it.message.contains("MUST contain a list of all resources required")
                }
            )
        }
    }

    @Test
    fun givenManifestResourcesDoNotExist_whenValidated_thenWillReturnErrors() {
        testValidationScenario(
            caseName = "case_manifest_resources_do_not_exist",
            caseResources = listOf(
                "app.html", "appmanifest.json",  "index.json", "grade1/grade1.json",
                "grade1/lesson001/lesson001.html", "grade1/lesson001/lesson001.json",
            ),
        ) {
            assertTrue(
                message = "Got error for manifest not listing resources",
                actual = reporter.messages.any {
                    it.level == ValidatorMessage.Level.ERROR &&
                            it.sourceUri == "$testBaseUrl/grade1/lesson001/lesson001.json" &&
                            it.message.contains("audio.ogg") &&
                            it.message.contains("Response status code not HTTP OK/200")
                }
            )
        }
    }

    @Test
    fun givenCacheValidationHeadersNotProvided_whenValidated_thenWillReturnErrors() {
        testValidationScenario(
            caseName = "case_valid",
            installConditionalHeaders = false,
        ) {
            assertTrue(
                message = "Got error for manifest not listing resources",
                actual = reporter.messages.any {
                    it.level == ValidatorMessage.Level.ERROR &&
                            it.message.contains("No last-modified or etag header found")
                }
            )
        }
    }

}