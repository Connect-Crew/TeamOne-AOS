package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteProjectFeedUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
): FlowUseCase<DeleteProjectFeedUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params): Flow<Unit> = flow {
        emit(projectRepository.deleteProjectFeed(params.projectId))
    }

    data class Params(
        val projectId: Long
    )
}