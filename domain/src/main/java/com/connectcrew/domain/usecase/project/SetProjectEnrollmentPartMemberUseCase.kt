package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetProjectEnrollmentPartMemberUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SetProjectEnrollmentPartMemberUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params) = flow {
        emit(projectRepository.setProjectEnrollmentPartMember(params.applyId, params.isPassed, params.leaderMessage))
    }

    data class Params(
        val applyId: Int,
        val isPassed: Boolean,
        val leaderMessage: String? = null
    )
}