package world.respect.server.routes.school.respect

import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import world.respect.server.AUTH_CONFIG_SCHOOL

fun Route.PersonRoute() {
    authenticate(AUTH_CONFIG_SCHOOL) {
        get("person") {
            val userIdPrincipal = call.principal<UserIdPrincipal>()
        }
    }


}