package com.connectcrew.data.token

import com.connectcrew.data.model.token.asExternalModel
import com.connectcrew.data.util.convertToAccessToken
import com.connectcrew.domain.preference.PreferenceStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

internal class TokenInterceptor @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            try {
                val token = preferenceStorage.token.first()?.asExternalModel()
                token?.accessToken
            } catch (e: Exception) {
                Timber.e("[TokenInterceptor Exception] :: ${e.message}")
                null
            }
        }

        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", accessToken.convertToAccessToken())
            .build()

        return chain.proceed(request)
    }
}