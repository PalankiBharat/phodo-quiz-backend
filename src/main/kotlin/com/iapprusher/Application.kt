package com.iapprusher

import com.iapprusher.application.data.copyclasses.readJsonFileExample
import com.iapprusher.application.di.configureDI
import com.iapprusher.application.plugins.configureMonitoring
import com.iapprusher.application.plugins.configureRouting
import com.iapprusher.application.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)


fun Application.module() {
    configureDI()
    configureSerialization()
    configureMonitoring()
    //configureHTTP()
    configureRouting()
}
