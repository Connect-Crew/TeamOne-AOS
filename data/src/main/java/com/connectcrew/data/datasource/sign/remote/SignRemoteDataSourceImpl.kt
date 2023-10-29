package com.connectcrew.data.datasource.sign.remote

import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.service.ServiceApi
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignRemoteDataSourceImpl @Inject constructor(
    private val serviceApi: ServiceApi
) : SignRemoteDataSource {

    override suspend fun signIn(accessToken: String, socialType: String): UserEntity {
        return try {
            serviceApi.signInForOauth(
                mapOf(
                    KEY_TOKEN to accessToken,
                    KEY_SOCIAL_TYPE to socialType
                )
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun signUp(accessToken: String, socialType: String, name: String, isAdNotificationAgree: Boolean): UserEntity {
        return try {
            serviceApi.signUpForOauth(
                mapOf(
                    KEY_TOKEN to accessToken,
                    KEY_SOCIAL_TYPE to socialType,
                    KEY_NAME to name,
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

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_SOCIAL_TYPE = "social"
        private const val KEY_NAME = "name"
        private const val KEY_TERMS_AGREEMENT = "termsAgreement"
        private const val KEY_PRIVACY_AGREEMENT = "privacyAgreement"
        private const val KEY_COMMUNITY_POLICY_AGREEMENT = "communityPolicyAgreement"
        private const val KEY_AD_NOTIFICATION_AGREEMENT = "adNotificationAgreement"
    }
}