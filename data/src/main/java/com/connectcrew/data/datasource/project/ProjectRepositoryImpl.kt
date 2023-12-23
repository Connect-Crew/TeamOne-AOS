package com.connectcrew.data.datasource.project

import com.connectcrew.data.datasource.project.remote.ProjectRemoteDataSource
import com.connectcrew.domain.usecase.project.ProjectRepository
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import javax.inject.Inject

internal class ProjectRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {

    override suspend fun getProjectInfo(): ProjectInfoContainerEntity {
        return remoteDataSource.getProjectInfo()
    }

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

    override suspend fun setProjectLike(accessToken: String?, projectId: Int): ProjectFeedLikeInfoEntity {
        return remoteDataSource.setProjectLike(accessToken, projectId)
    }

    override suspend fun setProjectEnrollment(accessToken: String?, projectId: Int, part: String, message: String) {
        return remoteDataSource.setProjectEnrollment(accessToken, projectId, part, message)
    }

    override suspend fun createProjectReport(accessToken: String?, projectId: Int, reportReason: String) {
        return remoteDataSource.createProjectReport(accessToken, projectId, reportReason)
    }
}