package com.iapprusher.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.iapprusher.application.data.entity.User
import com.iapprusher.application.data.request.GoogleSignInRequest
import com.iapprusher.application.data.request.RefreshTokenRequest
import com.iapprusher.application.data.response.AuthResponse
import com.iapprusher.application.data.response.BasicResponseModel
import com.iapprusher.application.data.response.TokenResponse
import com.iapprusher.application.data.response.failureResponse
import com.iapprusher.application.data.response.successResponse
import com.iapprusher.application.utils.JwtUtils
import com.iapprusher.application.utils.okResult
import com.iapprusher.application.utils.safeServerCall
import com.iapprusher.repo.user.UserRepo
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.util.logging.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.security.GeneralSecurityException
import java.util.*

class AuthService(
    private val userRepo: UserRepo,
    private val application: Application
) {
    val jwtSecret = application.environment.config.property("jwt.secret").getString()
    val jwtIssuer = application.environment.config.property("jwt.domain").getString()
    val jwtAudience = application.environment.config.property("jwt.audience").getString()
    val jwtRealm = application.environment.config.property("jwt.realm").getString()
    private val googleClientId = System.getenv("617499889029-nr7seakp8adtvuilhor024grlod2v07h.apps.googleusercontent.com") ?: "617499889029-nr7seakp8adtvuilhor024grlod2v07h.apps.googleusercontent.com"

    private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(Collections.singletonList(googleClientId))
        .build()

    suspend fun googleSignIn(request: GoogleSignInRequest): Pair<HttpStatusCode, BasicResponseModel<AuthResponse>> {
        return safeServerCall {
            // Verify the ID token
            var idToken: GoogleIdToken
            try {
                idToken = verifier.verify(request.idToken)
            }catch (ex: Exception) {
                return@safeServerCall okResult(failureResponse("Error verifying ID token: ${ex.localizedMessage}"))
            }

            val payload = idToken.payload
            val email = payload.email ?: ""
            val name = payload["name"] as? String ?: ""
            val pictureUrl = payload["picture"] as? String ?:""
            val googleId = payload.subject
            // Check if user exists
            var user = userRepo.getUserByGoogleId(googleId)
            val currentTime = Clock.System.now()

            if (user == null) {
                // Create new user
                user = User(
                    email = email,
                    name = name,
                    profilePicUrl = pictureUrl,
                    googleId = googleId,
                    createdAt = currentTime,
                    lastLoginAt = currentTime
                )
                val created = userRepo.createUser(user)
                if (!created) {
                    return@safeServerCall okResult(failureResponse("Failed to create user"))
                }
            } else {
                // Update last login time
                user = user.copy(lastLoginAt = currentTime)
                userRepo.updateUser(user.id.toString(), user)
            }

            // Generate tokens
            val accessToken = JwtUtils.generateAccessToken(user, jwtSecret, jwtIssuer, jwtAudience)
            val (refreshToken, _) = JwtUtils.generateRefreshToken(user, jwtSecret, jwtIssuer, jwtAudience)

            okResult(successResponse("Sign in successful", AuthResponse(
                user = user.toUserResponse(), 
                accessToken = accessToken,
                refreshToken = refreshToken
            )))
        }
    }

    suspend fun refreshToken(request: RefreshTokenRequest): Pair<HttpStatusCode, BasicResponseModel<TokenResponse>> {
        return safeServerCall {
            val refreshToken = request.refreshToken

            // Validate refresh token and get user ID
            val userId = JwtUtils.validateRefreshToken(refreshToken, jwtSecret, jwtIssuer, jwtAudience)
                ?: return@safeServerCall okResult(failureResponse("Invalid or expired refresh token"))

            // Get user by ID
            val user = userRepo.getUserById(userId)
                ?: return@safeServerCall okResult(failureResponse("User not found"))

            // Generate new tokens
            val accessToken = JwtUtils.generateAccessToken(user, jwtSecret, jwtIssuer, jwtAudience)
            val (newRefreshToken, _) = JwtUtils.generateRefreshToken(user, jwtSecret, jwtIssuer, jwtAudience)

            okResult(successResponse("Token refreshed successfully", TokenResponse(
                accessToken = accessToken,
                refreshToken = newRefreshToken
            )))
        }
    }
}
