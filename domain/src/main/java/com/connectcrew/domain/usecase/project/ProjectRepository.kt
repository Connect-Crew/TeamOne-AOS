package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity

interface ProjectRepository {

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

    suspend fun setProjectLike(accessToken: String?, projectId: Int): Boolean
}