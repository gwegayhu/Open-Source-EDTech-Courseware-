package world.respect.server.util.ext

import com.ustadmobile.ihttp.ext.clientProtocolAndHost
import com.ustadmobile.ihttp.headers.asIHttpHeaders
import io.ktor.http.Url
import io.ktor.server.application.ApplicationCall
import org.koin.core.scope.Scope
import org.koin.ktor.ext.getKoin
import world.respect.datalayer.respect.model.RespectRealm

/**
 * The virtual host being used. Used on the server to scope dependencies.
 */
val ApplicationCall.virtualHost: Url
    get() = Url(request.headers.asIHttpHeaders().clientProtocolAndHost())

/**
 * Respect Realms are handled using virtual hosting e.g. by subdomains. - see
 * AppKoinModule.
 */
fun ApplicationCall.getRealmKoinScope(): Scope {
    return getKoin().getOrCreateScope<RespectRealm>(virtualHost.toString())
}
