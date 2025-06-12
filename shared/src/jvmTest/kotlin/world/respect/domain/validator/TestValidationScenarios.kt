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

class TestValidationScenarios {

    @Rule
    @JvmField
    var tempFileRule = TemporaryFolder()

    @Test
    fun givenValidManifest() {
        val tempDir = tempFileRule.copyResourcesToTempDir(
            "/world/respect/validator/case_valid",
                listOf("app.html", "appmanifest.json",  "index.json", "grade1/grade1.json", "grade1/lesson001/lesson001.html", "grade1/lesson001/lesson001.json")
            )

        val port = findFreePort()
        println("Test running on port $port")
        tempDir.recursiveFindAndReplace(
            fileFilter =  { it.extension in listOf("html", "json") },
            textReplacement = {
                it.replace("\$TESTBASEURL", "http://localhost:$port/resources")
            }
        )

        val server = embeddedServer(Netty, port = port) {
            routing {
                staticFiles("/resources", tempDir)
            }
        }

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

        assertEquals(0, reporter.messages.count { it.isError })


        server.stop()


    }
}