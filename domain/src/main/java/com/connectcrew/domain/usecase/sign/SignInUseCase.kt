package com.connectcrew.domain.usecase.sign

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val signRepository: SignRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignInUseCase.Params, UserEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<UserEntity> = flow {
        emit(signRepository.signIn(params.accessToken, params.socialType))
    }

    data class Params(
        val accessToken: String,
        val socialType: String
    )
}