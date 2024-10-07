package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectMembersUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProjectMembersUseCase.Param, List<ProjectMemberEntity>>(ioDispatcher) {

    override fun execute(params: Param): Flow<List<ProjectMemberEntity>> = flow {
        emit(projectRepository.getProjectMembers(projectId = params.projectId))
    }

    data class Param(
        val projectId: Long
    )
}