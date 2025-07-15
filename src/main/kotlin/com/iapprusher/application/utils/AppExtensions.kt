package com.iapprusher.application.utils

import com.iapprusher.application.data.response.BasicResponseModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

/**
 * Extension function to get the user ID from the JWT token.
 * The user ID is stored in the subject claim of the JWT token.
 */
fun ApplicationCall.userId(): String? =
    this.principal<JWTPrincipal>()?.subject

/**
 * Extension function to get the user email from the JWT token.
 * The email is stored as a custom claim in the JWT token.
 */
fun ApplicationCall.userEmail(): String? =
    this.principal<JWTPrincipal>()?.getClaim("email", String::class)

/**
 * Extension function to get the user name from the JWT token.
 * The name is stored as a custom claim in the JWT token.
 */
fun ApplicationCall.userName(): String? =
    this.principal<JWTPrincipal>()?.getClaim("name", String::class)

suspend inline fun <reified T> ApplicationCall.sendResponse(pair: Pair<HttpStatusCode, BasicResponseModel<T>>) {
    this.respond(pair.first, pair.second)
}
