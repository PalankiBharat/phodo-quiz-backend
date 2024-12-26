package com.iapprusher.application.data.response

import kotlinx.serialization.Serializable

data class BasicResponseModel<T>(
    val status: String = "",
    val data: T? = null,
    val message: String = ""
)

inline fun <reified T> failureResponse(errorMessage: String): BasicResponseModel<T> {
    return BasicResponseModel(
        status = "Failure",
        message = errorMessage
    )
}

inline fun <reified T> successResponse(message: String, data: T?): BasicResponseModel<T> {
    return BasicResponseModel(
        status = "Success",
        data = data,
        message = message
    )
}