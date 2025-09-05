package world.respect.server.routes.school.respect

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.SchoolDataSource
import world.respect.server.util.ext.requireAccountScope
import world.respect.server.util.ext.respondDataLoadState
import kotlin.time.Instant

fun Route.PersonRoute(
    schoolDataSource: (ApplicationCall) -> SchoolDataSource = { call ->
        call.requireAccountScope().get()
    },
) {
        get("person") {
            val schoolDataSource = schoolDataSource(call)
            call.response.header(HttpHeaders.Vary, HttpHeaders.Authorization)
            val since = call.request.queryParameters["since"]?.let { Instant.parse(it) }
            call.respondDataLoadState(
                schoolDataSource.personDataSource.findAll(
                    DataLoadParams(), null, since
                )
            )
        }


}