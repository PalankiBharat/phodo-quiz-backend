package com.iapprusher.application.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }
}
