package world.respect.server.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import world.respect.server.domain.realm.add.AddRealmUseCase

const val AUTH_CONFIG_DIRECTORY_ADMIN_BASIC = "auth-directory-admin-basic"

fun Route.RespectRealmDirectoryRoute() {

    authenticate(AUTH_CONFIG_DIRECTORY_ADMIN_BASIC) {
        route("admin/") {
            //Add intercept authorization check

            post("add-realm") {
                val addRealmUseCase: AddRealmUseCase by inject()

                val addRealmRequest: AddRealmUseCase.AddRealmRequest = call.receive()
                val response = addRealmUseCase(addRealmRequest)
                call.respond(response)
            }
        }
    }

    get("realms") {

    }

    post("getinviteinfo") {

    }

}