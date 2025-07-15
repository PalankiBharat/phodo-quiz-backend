package com.iapprusher.repo.user

import com.iapprusher.application.data.entity.User

interface UserRepo {
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserByGoogleId(googleId: String): User?
    suspend fun getUserById(id: String): User?
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(id: String, user: User): User?
}
