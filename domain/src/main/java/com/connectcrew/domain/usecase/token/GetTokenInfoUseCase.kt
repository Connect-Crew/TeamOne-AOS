package com.connectcrew.domain.usecase.token

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.UseCase
import com.connectcrew.domain.usecase.token.entity.TokenInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTokenInfoUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, TokenInfoEntity>(ioDispatcher) {

    override suspend fun execute(params: Unit): TokenInfoEntity {
        return TokenInfoEntity(
            preferenceStorage.token.first(),
            preferenceStorage.socialType.first()
        )
    }
}