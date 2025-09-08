package world.respect.server.routes.school.respect

import androidx.paging.PagingSource
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import world.respect.datalayer.DataLayerParams
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.SchoolDataSource
import world.respect.server.util.ext.requireAccountScope
import world.respect.server.util.ext.respondOffsetLimitPaging

import kotlin.time.Instant

fun Route.PersonRoute(
    schoolDataSource: (ApplicationCall) -> SchoolDataSource = { call ->
        call.requireAccountScope().get()
    },
) {
    get("person") {
        val schoolDataSource = schoolDataSource(call)
        call.response.header(HttpHeaders.Vary, HttpHeaders.Authorization)
        val since = call.request.queryParameters[DataLayerParams.SINCE]?.let { Instant.parse(it) }

        val loadParams = PagingSource.LoadParams.Refresh(
            key = call.request.queryParameters[DataLayerParams.OFFSET]?.toInt() ?: 0,
            loadSize = call.request.queryParameters[DataLayerParams.LIMIT]?.toInt() ?: 1000,
            placeholdersEnabled = false
        )

        call.respondOffsetLimitPaging(
            params = loadParams,
            pagingSource = schoolDataSource.personDataSource.findAllAsPagingSource(
                loadParams = DataLoadParams(),
                searchQuery = null,
                since = since,
                guid = call.request.queryParameters[DataLayerParams.GUID],
            )
        )
    }

}