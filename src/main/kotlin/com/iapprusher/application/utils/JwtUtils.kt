package com.iapprusher.application.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.iapprusher.application.data.entity.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Utility class for working with JWT tokens in the application.
 * Provides functions to extract user information from JWT tokens and generate tokens.
 */
object JwtUtils {
    // Access token expiration time (15 minutes)
    private val ACCESS_TOKEN_EXPIRATION = 30.minutes

    // Refresh token expiration time (30 days)
    private val REFRESH_TOKEN_EXPIRATION = 30.days

    /**
     * Gets the user ID from the JWT token.
     * The user ID is stored in the subject claim of the JWT token.
     *
     * @param call The ApplicationCall containing the JWT principal
     * @return The user ID or null if not available
     */
    fun getUserId(call: ApplicationCall): String? =
        call.principal<JWTPrincipal>()?.subject

    /**
     * Gets the user email from the JWT token.
     * The email is stored as a custom claim in the JWT token.
     *
     * @param call The ApplicationCall containing the JWT principal
     * @return The user email or null if not available
     */
    fun getUserEmail(call: ApplicationCall): String? =
        call.principal<JWTPrincipal>()?.getClaim("email", String::class)

    /**
     * Gets the user name from the JWT token.
     * The name is stored as a custom claim in the JWT token.
     *
     * @param call The ApplicationCall containing the JWT principal
     * @return The user name or null if not available
     */
    fun getUserName(call: ApplicationCall): String? =
        call.principal<JWTPrincipal>()?.getClaim("name", String::class)

    /**
     * Generates an access token for the user.
     *
     * @param user The user for whom to generate the token
     * @param jwtSecret The secret key for signing the token
     * @param jwtIssuer The issuer of the token
     * @param jwtAudience The audience of the token
     * @return The generated access token
     */
    fun generateAccessToken(user: User, jwtSecret: String, jwtIssuer: String, jwtAudience: String): String {
        val expiresAt = Clock.System.now() + ACCESS_TOKEN_EXPIRATION
        return JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(jwtIssuer)
            .withAudience(jwtAudience)
            .withClaim("email", user.email)
            .withClaim("name", user.name)
            .withExpiresAt(Date.from(expiresAt.toJavaInstant()))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    /**
     * Generates a refresh token for the user.
     *
     * @param user The user for whom to generate the token
     * @param jwtSecret The secret key for signing the token
     * @param jwtIssuer The issuer of the token
     * @param jwtAudience The audience of the token
     * @return The generated refresh token and its expiration time
     */
    fun generateRefreshToken(user: User, jwtSecret: String, jwtIssuer: String, jwtAudience: String): Pair<String, Instant> {
        val expiresAt = Clock.System.now() + REFRESH_TOKEN_EXPIRATION
        val refreshToken = JWT.create()
            .withSubject(user.id.toString())
            .withIssuer(jwtIssuer)
            .withAudience(jwtAudience)
            .withExpiresAt(Date.from(expiresAt.toJavaInstant()))
            .sign(Algorithm.HMAC256(jwtSecret))
        return Pair(refreshToken, expiresAt)
    }

    /**
     * Checks if a refresh token is valid.
     *
     * @param refreshToken The refresh token to validate
     * @param jwtSecret The secret key for verifying the token
     * @param jwtIssuer The issuer of the token
     * @param jwtAudience The audience of the token
     * @return The user ID from the token if valid, null otherwise
     */
    fun validateRefreshToken(refreshToken: String, jwtSecret: String, jwtIssuer: String, jwtAudience: String): String? {
        return try {
            val verifier = JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .build()

            val decodedJWT = verifier.verify(refreshToken)
            decodedJWT.subject
        } catch (e: Exception) {
            null
        }
    }
}
