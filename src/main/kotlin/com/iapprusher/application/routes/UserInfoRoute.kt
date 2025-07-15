package com.iapprusher.application.routes

import com.iapprusher.application.data.response.successResponse
import com.iapprusher.application.utils.JwtUtils
import com.iapprusher.application.utils.Routes
import com.iapprusher.application.utils.sendResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Example route that demonstrates how to extract user information from JWT tokens.
 */
fun Route.userInfoRoute() {
    route(Routes.API_VERSION) {
        route("/user-info") {
            // This route will return the user information extracted from the JWT token
            get {
                val userId = JwtUtils.getUserId(call)
                val userEmail = JwtUtils.getUserEmail(call)
                val userName = JwtUtils.getUserName(call)
                
                val userInfo = mapOf(
                    "userId" to userId,
                    "email" to userEmail,
                    "name" to userName
                )
                
                call.sendResponse(Pair(HttpStatusCode.OK, successResponse("User info retrieved successfully", userInfo)))
            }
        }
    }
}