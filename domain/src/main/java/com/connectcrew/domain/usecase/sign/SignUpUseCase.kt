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
        emit(signRepository.signUp(params.accessToken, params.socialType, params.userName, params.isAdNotificationAgree))
    }

    data class Params(
        val accessToken: String,
        val socialType: String,
        val userName: String,
        val isAdNotificationAgree: Boolean
    )
}