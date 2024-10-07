package com.connectcrew.data.util

import com.connectcrew.domain.util.BadRequestException
import com.connectcrew.domain.util.ConflictException
import com.connectcrew.domain.util.ForbiddenException
import com.connectcrew.domain.util.NotFoundException
import com.connectcrew.domain.util.ServerErrorException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.UnknownErrorException
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.HttpException
import java.io.IOException

@JsonClass(generateAdapter = true)
internal data class ErrorResponseBody(
    @Json(name = "status")
    val code: Int,
    @Json(name = "message")
    val message: String
)

internal fun converterException(exception: Exception): Exception {
    return when (exception) {
        is IOException -> exception
        is HttpException -> {
            val errorBody = JsonUtil.getAdapter<ErrorResponseBody>().fromJson(exception.response()?.errorBody()?.string() ?: "")
            val code = (errorBody?.code) ?: (exception.response()?.code())
            val message = (errorBody?.message) ?: exception.response()?.message()
            val cause = exception.cause

            return when (code) {
                400 -> BadRequestException(message, cause)
                401 -> UnAuthorizedException(message, cause)
                403 -> ForbiddenException(message, cause)
                404 -> NotFoundException(message, cause)
                409 -> ConflictException(message, cause)
                500 -> ServerErrorException(message, cause)
                else -> UnknownErrorException(message, cause)
            }
        }

        else -> UnknownErrorException(exception.message, exception)
    }
}