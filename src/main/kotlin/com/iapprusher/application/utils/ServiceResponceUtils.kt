package com.iapprusher.application.utils

import com.iapprusher.application.data.response.BasicResponseModel
import com.iapprusher.application.data.response.failureResponse
import io.ktor.http.*

fun <T>okResult(response: BasicResponseModel<T>):Pair<HttpStatusCode, BasicResponseModel<T>>{
    return Pair(HttpStatusCode.OK,response)
}

inline fun <reified T>internalServerErrorResult():Pair<HttpStatusCode, BasicResponseModel<T>>{
    return Pair(HttpStatusCode.InternalServerError, failureResponse(StringConstants.BASIC_ERROR_MESSAGE))
}

fun <T>commonResult(statusCode: HttpStatusCode,response: BasicResponseModel<T>):Pair<HttpStatusCode, BasicResponseModel<T>>{
    return Pair(statusCode,response)
}