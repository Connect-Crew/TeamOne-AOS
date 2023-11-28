package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetProjectFeedLikeUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SetProjectFeedLikeUseCase.Params, ProjectFeedLikeInfoEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<ProjectFeedLikeInfoEntity> = flow {
        emit(projectRepository.setProjectLike(params.userToken, params.projectId))
    }

    data class Params(
        val userToken: String?,
        val projectId: Int
    )
}