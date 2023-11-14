package com.connectcrew.domain.usecase.user

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetUserInfoUseCase.Params, UserEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<UserEntity> = flow {
        emit(userRepository.getUserInfo(params.accessToken))
    }

    data class Params(
        val accessToken: String?
    )
}