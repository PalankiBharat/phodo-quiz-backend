package com.iapprusher

import com.iapprusher.application.data.copyclasses.readJsonFileExample
import com.iapprusher.application.di.configureDI
import com.iapprusher.application.plugins.configureHTTP
import com.iapprusher.application.plugins.configureMonitoring
import com.iapprusher.application.plugins.configureRouting
import com.iapprusher.application.plugins.configureSecurity
import com.iapprusher.application.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

/**
 * Main entry point for the application.
 * 
 * Note: For JWT token user information extraction, use the JwtUtils class:
 * - JwtUtils.getUserId(call): Gets the user ID from the JWT token
 * - JwtUtils.getUserEmail(call): Gets the user email from the JWT token
 * - JwtUtils.getUserName(call): Gets the user name from the JWT token
 * 
 * Example usage in a route:
 * ```
 * get("/protected") {
 *     val userId = JwtUtils.getUserId(call)
 *     call.respond("Your user ID is: $userId")
 * }
 * ```
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)


fun Application.module() {
    configureDI()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
