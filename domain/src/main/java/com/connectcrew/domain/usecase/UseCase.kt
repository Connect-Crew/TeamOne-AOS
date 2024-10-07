package com.connectcrew.domain.usecase

import com.connectcrew.domain.util.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in Params, out Type>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(params: Params): ApiResult<Type> {
        return try {
            withContext(coroutineDispatcher) {
                execute(params).let {
                    ApiResult.Success(it)
                }
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: Params): Type
}