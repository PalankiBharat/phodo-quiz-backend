package com.iapprusher.application.data.response

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val profilePicUrl: String? = null,
    val createdAt: String,
    val lastLoginAt: String
)

data class AuthResponse(
    val user: UserResponse,
    val accessToken: String,
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
