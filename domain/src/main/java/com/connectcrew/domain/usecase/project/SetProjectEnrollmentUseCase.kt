package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetProjectEnrollmentUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SetProjectEnrollmentUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params): Flow<Unit> = flow {
        emit(projectRepository.setProjectEnrollment(params.accessToken, params.projectId, params.enrollmentPart, params.enrollmentReason))
    }

    data class Params(
        val accessToken: String?,
        val projectId: Int,
        val enrollmentPart: String,
        val enrollmentReason: String
    )
}