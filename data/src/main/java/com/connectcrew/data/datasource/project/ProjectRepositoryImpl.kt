package com.connectcrew.data.datasource.project

import com.connectcrew.data.datasource.project.remote.ProjectRemoteDataSource
import com.connectcrew.domain.usecase.project.ProjectRepository
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import javax.inject.Inject

internal class ProjectRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {

    override suspend fun getProjectFeeds(
        accessToken: String?,
        lastId: Int?,
        loadSize: Int,
        goal: String?,
        career: String?,
        region: String?,
        isOnline: Boolean?,
        part: String?,
        skills: String?,
        states: String?,
        category: String?
    ): List<ProjectFeedEntity> {
        return remoteDataSource.getProjectFeeds(accessToken, lastId, loadSize, goal, career, region, isOnline, part, skills, states, category)
    }

    override suspend fun getProjectFeedDetail(accessToken: String?, projectId: Int): ProjectFeedDetailEntity {
        return remoteDataSource.getProjectFeedDetail(accessToken, projectId)
    }

    override suspend fun setProjectLike(accessToken: String?, projectId: Int): Boolean {
        return remoteDataSource.setProjectLike(accessToken, projectId)
    }
}