package com.connectcrew.data.datasource.project.remote

import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity

internal interface ProjectRemoteDataSource {

    suspend fun getProjectFeeds(
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
    ): List<ProjectFeedEntity>

    suspend fun getProjectFeedDetail(accessToken: String?, projectId: Int): ProjectFeedDetailEntity

    suspend fun setProjectLike(accessToken: String?, projectId: Int): Boolean

    suspend fun setProjectEnrollment(accessToken: String?, projectId: Int, part: String, message: String)

    suspend fun createProjectReport(accessToken: String?, projectId: Int, reportReason: String)
}