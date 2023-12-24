package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.RequestRecruitStatusEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateProjectFeedUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<CreateProjectFeedUseCase.Params, Long>(ioDispatcher) {

    override fun execute(params: Params): Flow<Long> = flow {
        emit(
            projectRepository.createProjectFeed(
                params.title,
                params.region,
                params.isOnline,
                params.state,
                params.careerMin,
                params.careerMax,
                params.leaderPart,
                params.category,
                params.goal,
                params.introduction,
                params.recruits,
                params.skills,
                params.bannerImageUrls
            )
        )
    }

    data class Params(
        val title: String,
        val region: String,
        val isOnline: Boolean,
        val state: String,
        val careerMin: String,
        val careerMax: String,
        val leaderPart: String,
        val category: List<String>,
        val goal: String,
        val introduction: String,
        val recruits: List<RequestRecruitStatusEntity>,
        val skills: List<String>,
        val bannerImageUrls: List<String>
    )
}