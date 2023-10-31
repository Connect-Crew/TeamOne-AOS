package com.connectcrew.domain.usecase.sign

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signRepository: SignRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignUpUseCase.Params, UserEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<UserEntity> = flow {
        emit(
            signRepository.signUp(
                accessToken = params.accessToken,
                socialType = params.socialType,
                userName = params.userName,
                nickname = params.nickname,
                email = params.email,
                profileUrl = params.profileUrl,
                isAdNotificationAgree = params.isAdNotificationAgree
            )
        )
    }

    data class Params(
        val accessToken: String,
        val socialType: String,
        val userName: String? = null,
        val nickname: String,
        val email: String?,
        val profileUrl: String?,
        val isAdNotificationAgree: Boolean,
    )
}