package world.respect.server.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.coroutines.flow.first
import org.koin.ktor.ext.inject
import world.respect.datalayer.schooldirectory.SchoolDirectoryDataSourceLocal
import world.respect.server.domain.school.add.AddSchoolUseCase

const val AUTH_CONFIG_DIRECTORY_ADMIN_BASIC = "auth-directory-admin-basic"

fun Route.RespectSchoolDirectoryRoute() {

    get("school") {
        //TODO: @Nikunj Check/implement this
        val directoryDataSource: SchoolDirectoryDataSourceLocal by inject()
        val query = call.request.queryParameters["q"]
        val schoolsFound = directoryDataSource.searchSchools(query ?: "").first()
        call.respond(schoolsFound)
    }

    authenticate(AUTH_CONFIG_DIRECTORY_ADMIN_BASIC) {
        post("school") {
            val addSchoolUseCase: AddSchoolUseCase by inject()

            val addSchoolRequest: AddSchoolUseCase.AddSchoolRequest = call.receive()
            val response = addSchoolUseCase(addSchoolRequest)
            call.respond(response)
        }
    }

    post("getinviteinfo") {

    }

}