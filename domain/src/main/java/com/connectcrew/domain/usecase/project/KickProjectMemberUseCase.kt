package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.KickReasonEntity
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KickProjectMemberUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<KickProjectMemberUseCase.Params, ProjectMemberEntity>(ioDispatcher) {

    override fun execute(params: Params): Flow<ProjectMemberEntity> = flow {
        emit(
            projectRepository.kickProjectMember(
                projectId = params.projectId,
                userId = params.userId,
                reasons = params.reasons
            )
        )
    }

    data class Params(
        val projectId: Long,
        val userId: Int,
        val reasons: List<KickReasonEntity>
    )
}