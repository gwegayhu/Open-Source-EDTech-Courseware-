package world.respect.server.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.getKoin
import world.respect.datalayer.RespectAppDataSource
import world.respect.server.util.ext.respondDataReadyState
import world.respect.server.util.ext.virtualHost

/**
 * Serve the RespectRealm as a JSON according to the virtual host
 *
 * @param path typically "respect-realm.json"
 */
fun Route.getRespectRealmJson(path: String) {
    val appDataSource: RespectAppDataSource = getKoin().get()

    get(path) {
        val realmServerUrl = call.virtualHost
        val realm = appDataSource.realmDirectoryDataSource.getRealmByUrl(realmServerUrl)
        if(realm != null) {
            call.respondDataReadyState(realm)
        }else {
            call.respondText(
                text = "Not found: $realmServerUrl",
                status = HttpStatusCode.NotFound
            )
        }
    }
}