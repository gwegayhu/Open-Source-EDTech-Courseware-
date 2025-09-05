package world.respect.server.util.ext

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fromHttpToGmtDate
import io.ktor.http.toHttpDate
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.util.date.GMTDate
import org.koin.core.scope.Scope
import org.koin.ktor.ext.getKoin
import world.respect.datalayer.AuthenticatedUserPrincipalId
import world.respect.datalayer.DataLayerHeaders
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.ext.lastModifiedForHttpResponseHeader
import world.respect.datalayer.respect.model.SchoolDirectoryEntry
import world.respect.libutil.util.throwable.ForbiddenException
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.util.di.RespectAccountScopeId
import world.respect.shared.util.di.SchoolDirectoryEntryScopeId
import kotlin.time.Instant

/**
 * The virtual host being used. Used on the server to scope dependencies.
 */
val ApplicationCall.virtualHost: Url
    get() = request.virtualHost

/**
 * Respect schools are handled using virtual hosting e.g. by subdomains. - see
 * SchoolDirectoryEntryScopeId and AppKoinModule.
 */
fun ApplicationCall.getSchoolKoinScope(): Scope {
    return getKoin().getOrCreateScope<SchoolDirectoryEntry>(
        SchoolDirectoryEntryScopeId(
            request.virtualHost, null
        ).scopeId
    )
}

fun ApplicationCall.requireAccountScope(): Scope {
    val authPrincipalId: UserIdPrincipal = principal() ?:
        throw ForbiddenException("Not authenticated")

    val scopeId = RespectAccountScopeId(
        request.virtualHost,
        AuthenticatedUserPrincipalId(authPrincipalId.name)
    ).scopeId

    return getKoin().getScopeOrNull(scopeId) ?: getKoin().createScope<RespectAccount>(scopeId).also {
        it.linkTo(getSchoolKoinScope())
    }
}


/**
 * Handles a response given a DataReadyState. Will automatically handle responding with 304 not
 * modified if the request has an If-Modified-Since header or If-None-Match header.
 *
 * It will also add the etag and last modified dates from the DataReadyState metainfo to the response.
 */
suspend inline fun <reified T: Any> ApplicationCall.respondDataLoadState(
    dataLoadState: DataLoadState<T>
) {
    dataLoadState.metaInfo.etag?.also {
        response.header(HttpHeaders.ETag, it)
    }

    val lastModTimeStamp = dataLoadState.lastModifiedForHttpResponseHeader()

    lastModTimeStamp?.also {
        response.header(HttpHeaders.LastModified, GMTDate(it).toHttpDate())
    }

    if(dataLoadState.metaInfo.consistentThrough > 0) {
        response.header(
            name = DataLayerHeaders.XConsistentThrough,
            value = Instant.fromEpochMilliseconds(dataLoadState.metaInfo.consistentThrough)
                .toString()
        )
    }

    //If-Modified-Since headers are really only accurate to the nearest second, so we need to convert
    //to seconds
    val ifModifiedSinceRequestTimestamp = request.headers[HttpHeaders.IfModifiedSince]
        ?.fromHttpToGmtDate()?.timestamp?.let { it / 1_000 }

    if(ifModifiedSinceRequestTimestamp != null && lastModTimeStamp != null &&
        ifModifiedSinceRequestTimestamp >= (lastModTimeStamp / 1_000)
    ) {
        respond(HttpStatusCode.NotModified)
        return
    }

    val ifNoneMatchRequestHeader = request.headers[HttpHeaders.IfNoneMatch]
    if(ifNoneMatchRequestHeader != null &&
        ifNoneMatchRequestHeader == dataLoadState.metaInfo.etag
    ) {
        respond(HttpStatusCode.NotModified)
        return
    }

    if(dataLoadState is DataReadyState) {
        respond(dataLoadState.data)
    }else {
        respond(HttpStatusCode.ServiceUnavailable)
    }

}
