package world.respect.server

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import net.sourceforge.argparse4j.inf.Namespace
import java.io.File
import java.util.Properties
import kotlin.system.exitProcess

fun managerServerMain(ns: Namespace) {
    val json = Json { encodeDefaults = true }
    val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json = json)
        }
    }

    val dataDir = ns.getString("datadir")?.let { File(it) }
        ?: File("${ktorAppHomeDir().absolutePath}/$DEFAULT_DATA_DIR_NAME")
    println("DataDir=$dataDir")

    val serverPropertiesFile = ktorServerPropertiesFile(dataDir = dataDir)

    if(!serverPropertiesFile.exists()) {
        println("Error: Server is not running: server.properties does not exist")
        exitProcess(1)
    }

    val serverProperties = Properties()
    serverPropertiesFile.reader().use { serverPropertiesReader ->
        serverProperties.load(serverPropertiesReader)
    }

    val port = serverProperties.getProperty(SERVER_PROPERTIES_KEY_PORT)

    val systemConfigAuth = File(dataDir, DIRECTORY_ADMIN_FILENAME).readText().trim()

    println("Connect to $port using auth $systemConfigAuth")

    when(ns.getString("subparser_name")) {
        CMD_ADD_REALM -> {

        }
    }
}