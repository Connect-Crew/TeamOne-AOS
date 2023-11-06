package com.connectcrew.domain.usecase.token

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUpdatedTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, String?>(ioDispatcher) {

    override fun execute(params: Unit): Flow<String?> = flow {
        emit(tokenRepository.getTokenUsingUserInfo(preferenceStorage.user.first()))
    }
}