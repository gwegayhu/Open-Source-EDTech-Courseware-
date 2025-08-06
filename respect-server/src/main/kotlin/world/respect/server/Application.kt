package world.respect.server

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import world.respect.Greeting
import world.respect.libutil.ext.randomString
import world.respect.server.routes.AUTH_CONFIG_DIRECTORY_ADMIN_BASIC
import world.respect.server.routes.RespectRealmDirectoryRoute
import java.io.File
import java.util.Properties

@Suppress("unused") // Used via application.conf
fun Application.module() {

    val serverProperties = Properties().apply {
        setProperty(SERVER_PROPERTIES_KEY_PORT, environment.config.port.toString())
    }

    environment.config.absoluteDataDir().takeIf { !it.exists() }?.mkdirs()

    ktorServerPropertiesFile(
        dataDir = environment.config.absoluteDataDir()
    ).writer().use { serverPropWriter ->
        serverProperties.store(serverPropWriter, null)
    }

    val dirAdminFile = File(environment.config.absoluteDataDir(), DIRECTORY_ADMIN_FILENAME)
    dirAdminFile.takeIf { !it.exists() }?.also {
        it.writeText(randomString(DEFAULT_DIR_ADMIN_PASS_LENGTH))
    }

    val json = Json {
        ignoreUnknownKeys = true
    }

    install(Koin) {
        slf4jLogger()
        modules(serverKoinModule(environment.config, json))
    }

    install(ContentNegotiation) {
        json(
            json = json,
            contentType = ContentType.Application.Json
        )
    }

    install(Authentication) {
        basic(AUTH_CONFIG_DIRECTORY_ADMIN_BASIC) {
            realm = "Access realm directory admin"
            validate { credentials ->
                val adminPassword = dirAdminFile.readText()
                if(credentials.password == adminPassword) {
                    UserIdPrincipal(credentials.name)
                }else {
                    null
                }
            }
        }
    }


    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        route("directory") {
            RespectRealmDirectoryRoute()
        }
    }
}
