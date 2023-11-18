package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectFeedDetailUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProjectFeedDetailUseCase.Params, ProjectFeedDetailEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<ProjectFeedDetailEntity> = flow {
        emit(projectRepository.getProjectFeedDetail(params.accessToken, params.projectId))
    }

    data class Params(
        val accessToken: String?,
        val projectId: Int
    )
}