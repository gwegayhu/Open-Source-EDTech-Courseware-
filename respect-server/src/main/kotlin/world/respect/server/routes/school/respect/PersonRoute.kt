package world.respect.server.routes.school.respect

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.SchoolDataSource
import world.respect.server.AUTH_CONFIG_SCHOOL
import world.respect.server.util.ext.requireAccountScope
import world.respect.server.util.ext.respondDataLoadState

fun Route.PersonRoute() {
    authenticate(AUTH_CONFIG_SCHOOL) {
        get("person") {
            val accountScope = call.requireAccountScope()
            val schoolDataSource = accountScope.get<SchoolDataSource>()
            call.respondDataLoadState(
                schoolDataSource.personDataSource.findAll(DataLoadParams(), null)
            )
        }
    }


}