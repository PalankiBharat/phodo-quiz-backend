package com.iapprusher.repo.user

import com.iapprusher.application.data.entity.User
import com.iapprusher.application.utils.StringConstants.ID
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Instant
import org.bson.types.ObjectId

class UserRepoImpl(
    mongoCollection: MongoCollection<User>
) : UserRepo {
    private val collection = mongoCollection.withDocumentClass(User::class.java)

    override suspend fun getUserByEmail(email: String): User? {
        return collection.find(Filters.eq(User::email.name, email)).firstOrNull()
    }

    override suspend fun getUserByGoogleId(googleId: String): User? {
        return collection.find(Filters.eq(User::googleId.name, googleId)).firstOrNull()
    }

    override suspend fun getUserById(id: String): User? {
        return collection.find(Filters.eq(ID, ObjectId(id))).firstOrNull()
    }

    override suspend fun createUser(user: User): Boolean {
        return collection.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUser(id: String, user: User): User? {
        return collection.findOneAndReplace(Filters.eq(ID, ObjectId(id)), user)
    }
}
