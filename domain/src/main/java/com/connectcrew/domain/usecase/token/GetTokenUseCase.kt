package com.connectcrew.domain.usecase.token

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
): UseCase<Unit, String?>(ioDispatcher) {

    override suspend fun execute(params: Unit): String? {
        return preferenceStorage.token.first()
    }
}