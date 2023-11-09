package com.connectcrew.domain.usecase.project

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProjectFeedsUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<GetProjectFeedsUseCase.Params, PagingData<ProjectFeedEntity>>(ioDispatcher) {

    override fun execute(params: Params): Flow<PagingData<ProjectFeedEntity>> = Pager(
        config = PagingConfig(pageSize = params.loadSize, enablePlaceholders = true)
    ) {
        object : PagingSource<Int, ProjectFeedEntity>() {
            override fun getRefreshKey(state: PagingState<Int, ProjectFeedEntity>): Int? = null

            override suspend fun load(loadParams: LoadParams<Int>): LoadResult<Int, ProjectFeedEntity> {
                val page = loadParams.key

                return try {
                    projectRepository.getProjectFeeds(
                        accessToken = params.userToken,
                        lastId = page,
                        loadSize = params.loadSize,
                        goal = params.goal,
                        career = params.career,
                        region = params.region,
                        isOnline = params.isOnline,
                        part = params.part,
                        skills = params.skills,
                        states = params.states,
                        category = params.category,
                    ).let {
                        LoadResult.Page(
                            data = it,
                            prevKey = null,
                            nextKey = if (it.size == params.loadSize) it.last().id else null
                        )
                    }
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }
        }
    }.flow

    data class Params(
        val userToken: String?,
        val loadSize: Int = PROJECT_FEED_LOAD_SIZE,
        val lastId: Int? = null,
        val goal: String? = null,
        val career: String? = null,
        val region: String? = null,
        val isOnline: Boolean? = null,
        val part: String? = null,
        val skills: String? = null,
        val states: String? = null,
        val category: String? = null
    )

    companion object {
        private const val PROJECT_FEED_LOAD_SIZE = 40
    }
}