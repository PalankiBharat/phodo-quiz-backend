package com.iapprusher.application.data.request

import kotlinx.serialization.Serializable

/**
 * Request model for refreshing an access token using a refresh token.
 *
 * @property refreshToken The refresh token to use for obtaining a new access token
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)