package world.respect.server

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import world.respect.Greeting
import world.respect.libutil.ext.randomString
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

    install(Koin) {
        slf4jLogger()
        modules(serverKoinModule(environment.config))
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        RespectRealmDirectoryRoute()
    }
}
