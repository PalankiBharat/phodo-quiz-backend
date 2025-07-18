package com.iapprusher.application.plugins

import com.iapprusher.application.routes.authRoute
import com.iapprusher.application.routes.questionRoute
import com.iapprusher.application.routes.tagRoute
import com.iapprusher.application.routes.userInfoRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // Public routes (no authentication required)
        route("/") {
            authRoute()
        }

        // Protected routes (JWT authentication required)
        authenticate("auth-jwt") {
            route("/") {
                questionRoute()
                tagRoute()
                userInfoRoute() // Example route that demonstrates JWT user info extraction
            }
        }
    }
}
