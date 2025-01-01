package com.iapprusher.application.plugins

import com.iapprusher.application.routes.questionRoute
import com.iapprusher.application.routes.tagRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        questionRoute()
        tagRoute()
    }
}
