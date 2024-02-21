package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectEnrollmentMembersUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProjectEnrollmentMembersUseCase.Params, List<ProjectEnrollmentMemberEntity>>(ioDispatcher) {

    override fun execute(params: Params): Flow<List<ProjectEnrollmentMemberEntity>> = flow {
        emit(projectRepository.gerProjectEnrollmentMembers(params.projectId))
    }

    data class Params(
        val projectId: Long
    )
}