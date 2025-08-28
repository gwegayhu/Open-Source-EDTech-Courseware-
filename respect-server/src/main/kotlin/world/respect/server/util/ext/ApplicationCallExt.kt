package world.respect.server.util.ext

import com.ustadmobile.ihttp.ext.clientProtocolAndHost
import com.ustadmobile.ihttp.headers.asIHttpHeaders
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fromHttpToGmtDate
import io.ktor.http.toHttpDate
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.util.date.GMTDate
import org.koin.core.scope.Scope
import org.koin.ktor.ext.getKoin
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.respect.model.SchoolDirectoryEntry

/**
 * The virtual host being used. Used on the server to scope dependencies.
 */
val ApplicationCall.virtualHost: Url
    get() = Url(request.headers.asIHttpHeaders().clientProtocolAndHost())

/**
 * Respect Realms are handled using virtual hosting e.g. by subdomains. - see
 * AppKoinModule.
 */
fun ApplicationCall.getSchoolKoinScope(): Scope {
    return getKoin().getOrCreateScope<SchoolDirectoryEntry>(virtualHost.toString())
}

/**
 * Handles a response given a DataReadyState. Will automatically handle responding with 304 not
 * modified if the request has an If-Modified-Since header or If-None-Match header.
 *
 * It will also add the etag and last modified dates from the DataReadyState metainfo to the response.
 */
suspend inline fun <reified T: Any> ApplicationCall.respondDataReadyState(
    dataReadyState: DataReadyState<T>
) {
    dataReadyState.metaInfo.etag?.also {
        response.header(HttpHeaders.ETag, it)
    }
    dataReadyState.metaInfo.lastModified.takeIf { it > 0 }?.also {
        response.header(HttpHeaders.LastModified, GMTDate(it).toHttpDate())
    }

    //If-Modified-Since headers are really only accurate to the nearest second, so we need to convert
    //to seconds
    val ifModifiedSinceRequestTimestamp = request.headers[HttpHeaders.IfModifiedSince]
        ?.fromHttpToGmtDate()?.timestamp?.let { it / 1_000 }

    if(ifModifiedSinceRequestTimestamp != null &&
        ifModifiedSinceRequestTimestamp >= (dataReadyState.metaInfo.lastModified / 1_000)
    ) {
        respond(HttpStatusCode.NotModified)
        return
    }

    val ifNoneMatchRequestHeader = request.headers[HttpHeaders.IfNoneMatch]
    if(ifNoneMatchRequestHeader != null &&
        ifNoneMatchRequestHeader == dataReadyState.metaInfo.etag
    ) {
        respond(HttpStatusCode.NotModified)
        return
    }

    respond(dataReadyState.data)
}
