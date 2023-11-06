package com.connectcrew.data.datasource.sign.remote

import com.connectcrew.data.BuildConfig
import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.service.AuthApi
import com.connectcrew.data.service.ExternalApi
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi,
    private val externalApi: ExternalApi
) : SignRemoteDataSource {

    override suspend fun signIn(accessToken: String, socialType: String): UserEntity {
        return try {
            authApi.signInForOauth(
                mapOf(
                    KEY_TOKEN to accessToken,
                    KEY_SOCIAL_TYPE to socialType
                )
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun signUp(
        accessToken: String,
        socialType: String,
        username: String?,
        nickname: String,
        email: String?,
        profileUrl: String?,
        isAdNotificationAgree: Boolean
    ): UserEntity {
        return try {
            authApi.signUpForOauth(
                mapOf(
                    KEY_TOKEN to accessToken,
                    KEY_SOCIAL_TYPE to socialType,
                    KEY_USER_NAME to username,
                    KEY_NICKNAME to nickname,
                    KEY_EMAIL to email,
                    KEY_PROFILE to profileUrl,
                    KEY_TERMS_AGREEMENT to true,
                    KEY_PRIVACY_AGREEMENT to true,
                    KEY_COMMUNITY_POLICY_AGREEMENT to true,
                    KEY_AD_NOTIFICATION_AGREEMENT to isAdNotificationAgree
                )
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun getGoogleTokenInfo(authCode: String): String {
        return try {
            externalApi.getGoogleAccessToken(
                mapOf(
                    KEY_GOOGLE_GRANT_TYPE to "authorization_code",
                    KEY_GOOGLE_CLIENT_ID to BuildConfig.GOOGLE_CLIENT_ID_DATA,
                    KEY_GOOGLE_CLIENT_SECRET to BuildConfig.GOOGLE_CLIENT_SECRET_DATA,
                    KEY_GOOGLE_REDIRECT_URI to "",
                    KEY_GOOGLE_AUTH_CODE to authCode,
                )
            ).accessToken
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_SOCIAL_TYPE = "social"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_USER_NAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PROFILE = "profile"
        private const val KEY_TERMS_AGREEMENT = "termsAgreement"
        private const val KEY_PRIVACY_AGREEMENT = "privacyAgreement"
        private const val KEY_COMMUNITY_POLICY_AGREEMENT = "communityPolicyAgreement"
        private const val KEY_AD_NOTIFICATION_AGREEMENT = "adNotificationAgreement"

        private const val KEY_GOOGLE_GRANT_TYPE = "grant_type"
        private const val KEY_GOOGLE_CLIENT_ID = "client_id"
        private const val KEY_GOOGLE_CLIENT_SECRET = "client_secret"
        private const val KEY_GOOGLE_REDIRECT_URI = "redirect_uri"
        private const val KEY_GOOGLE_AUTH_CODE = "code"
    }
}