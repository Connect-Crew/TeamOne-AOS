package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateProjectReportUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<CreateProjectReportUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params) = flow {
        emit(projectRepository.createProjectReport(params.projectId, params.reportReason))
    }

    data class Params(
        val projectId: Long,
        val reportReason: String
    )
}