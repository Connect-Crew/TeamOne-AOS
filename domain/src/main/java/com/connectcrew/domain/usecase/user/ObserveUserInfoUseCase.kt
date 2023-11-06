package com.connectcrew.domain.usecase.user

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveUserInfoUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, UserEntity?>(ioDispatcher) {

    override fun execute(params: Unit): Flow<UserEntity?> = flow {
        emitAll(preferenceStorage.user)
    }
}