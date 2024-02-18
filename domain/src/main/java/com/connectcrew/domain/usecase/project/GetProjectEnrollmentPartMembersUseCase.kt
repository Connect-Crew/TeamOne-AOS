package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProjectEnrollmentPartMembersUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProjectEnrollmentPartMembersUseCase.Params, List<ProjectEnrollmentPartMemberEntity>>(ioDispatcher) {

    override fun execute(params: Params): Flow<List<ProjectEnrollmentPartMemberEntity>> = flow {
        emit(projectRepository.getProjectEnrollmentPartMembers(params.projectId, params.partKey))
    }

    data class Params(
        val projectId: Long,
        val partKey: String
    )
}