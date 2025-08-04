package world.respect.server

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import world.respect.Greeting

@Suppress("unused") // Used via application.conf
fun Application.module() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
}
