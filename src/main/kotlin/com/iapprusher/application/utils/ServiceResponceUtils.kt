package com.iapprusher.application.utils

import com.iapprusher.application.data.response.BasicResponseModel
import com.iapprusher.application.data.response.failureResponse
import com.mongodb.MongoBulkWriteException
import com.mongodb.MongoServerException
import com.mongodb.MongoWriteConcernException
import com.mongodb.MongoWriteException
import io.ktor.http.*

fun <T> okResult(response: BasicResponseModel<T>): Pair<HttpStatusCode, BasicResponseModel<T>> {
    return Pair(HttpStatusCode.OK, response)
}

inline fun <reified T> internalServerErrorResult(e: Exception? = null): Pair<HttpStatusCode, BasicResponseModel<T>> {
    val errorMessage: String = when (e) {
        is MongoWriteException -> {
            e.error.message
        }
        is MongoWriteConcernException -> {
            e.writeConcernError.message
        }
        is MongoBulkWriteException -> {
            e.writeErrors.joinToString { " " }
        }
        else -> {
            e?.message ?: ""
        }
    }

    return Pair(
        HttpStatusCode.InternalServerError,
        failureResponse("${StringConstants.BASIC_ERROR_MESSAGE} $errorMessage")
    )
}

fun <T> commonResult(
    statusCode: HttpStatusCode,
    response: BasicResponseModel<T>
): Pair<HttpStatusCode, BasicResponseModel<T>> {
    return Pair(statusCode, response)
}