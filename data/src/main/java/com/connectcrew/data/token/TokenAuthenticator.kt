package com.connectcrew.data.token

import com.connectcrew.data.model.token.asEntity
import com.connectcrew.data.service.TokenApi
import com.connectcrew.data.util.convertToAccessToken
import com.connectcrew.domain.preference.PreferenceStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

internal class TokenAuthenticator @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val tokenApi: TokenApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = preferenceStorage.token.first()?.refreshToken
            try {
                val token = tokenApi.getRefreshToken(refreshToken.convertToAccessToken())
                preferenceStorage.saveUserToken(token.asEntity())

                response.request.newBuilder()
                    .header("Authorization", token.accessToken.convertToAccessToken())
                    .build()
            } catch (e: Exception) {
                Timber.e("TokenAuthenticator Error :: ${e.message}")
                null
            }
        }
    }
}