package com.iapprusher.application.utils

import com.iapprusher.application.data.response.BasicResponseModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

/*fun ApplicationCall.userId(): String? =
    this.principal<JWTPrincipal>()?.getClaim(USER_ID, String::class)*/

suspend inline fun <reified T> ApplicationCall.sendResponse(pair: Pair<HttpStatusCode, BasicResponseModel<T>>) {
    this.respond(pair.first, pair.second)
}
