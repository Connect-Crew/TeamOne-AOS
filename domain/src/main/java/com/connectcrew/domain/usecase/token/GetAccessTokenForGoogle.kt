package com.connectcrew.domain.usecase.token

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.sign.SignRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAccessTokenForGoogle @Inject constructor(
    private val signRepository: SignRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetAccessTokenForGoogle.Params, String>(ioDispatcher) {

    override fun execute(params: Params): Flow<String> = flow {
        emit(signRepository.getGoogleTokenInfo(params.authCode))
    }

    data class Params(
        val authCode: String
    )
}