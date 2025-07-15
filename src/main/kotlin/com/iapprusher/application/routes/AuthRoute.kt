package com.iapprusher.application.routes

import com.iapprusher.application.data.request.GoogleSignInRequest
import com.iapprusher.application.data.request.RefreshTokenRequest
import com.iapprusher.application.utils.Routes
import com.iapprusher.application.utils.sendResponse
import com.iapprusher.service.AuthService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoute() {
    val authService: AuthService by application.inject()

    route(Routes.API_VERSION) {
        route(Routes.Auth.ROUTE) {
            googleSignIn(authService)
            refreshToken(authService)
        }
    }
}

/**
 * POST /api/v1/auth/google-signin
 * Authenticates a user with Google Sign-in
 *
 * @request body GoogleSignInRequest - Contains the ID token from Google
 */
private fun Route.googleSignIn(authService: AuthService) {
    post(Routes.Auth.GOOGLE_SIGNIN) {
        val request = call.receive<GoogleSignInRequest>()
        val response = authService.googleSignIn(request)
        call.sendResponse(response)
    }
}

/**
 * POST /api/v1/auth/refresh-token
 * Refreshes an access token using a refresh token
 *
 * @request body RefreshTokenRequest - Contains the refresh token
 */
private fun Route.refreshToken(authService: AuthService) {
    post(Routes.Auth.REFRESH_TOKEN) {
        val request = call.receive<RefreshTokenRequest>()
        val response = authService.refreshToken(request)
        call.sendResponse(response)
    }
}
