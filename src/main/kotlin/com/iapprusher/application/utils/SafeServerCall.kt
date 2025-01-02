package com.iapprusher.application.utils

import com.iapprusher.application.data.response.BasicResponseModel
import io.ktor.http.*


suspend inline fun <reified T> safeServerCall(serverCall: suspend () -> Pair<HttpStatusCode, BasicResponseModel<T>>): Pair<HttpStatusCode, BasicResponseModel<T>> {
    return try {
        serverCall()
    } catch (e: Exception) {
        println("Exception" + e.localizedMessage)
        internalServerErrorResult(e)
    }
}