package com.connectcrew.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class ApiResult<out R> {
    data object Loading : ApiResult<Nothing>()

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(val exception: Exception) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading"
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

val ApiResult<*>.succeeded
    get() = this is ApiResult.Success && data != null

val <T> ApiResult<T>.data: T?
    get() = (this as? ApiResult.Success)?.data

fun <T> Flow<T>.asResult(): Flow<ApiResult<T>> {
    return this
        .map<T, ApiResult<T>> { ApiResult.Success(it) }
        .onStart { emit(ApiResult.Loading) }
        .catch { emit(ApiResult.Error(it as? Exception ?: Exception(it))) }
}
