package com.iapprusher.application.data.entity

import com.iapprusher.application.data.response.UserResponse
import com.iapprusher.application.utils.InstantAsBsonDateTime
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId val id: ObjectId? = null,
    val email: String,
    val name: String,
    val profilePicUrl: String? = null,
    val googleId: String,
    val createdAt: Instant,
    val lastLoginAt: Instant
) {
    fun toUserResponse(): UserResponse {
        return UserResponse(
            id = id?.toString() ?: "",
            email = email,
            name = name,
            profilePicUrl = profilePicUrl,
            createdAt = createdAt.toString(),
            lastLoginAt = lastLoginAt.toString()
        )
    }
}
