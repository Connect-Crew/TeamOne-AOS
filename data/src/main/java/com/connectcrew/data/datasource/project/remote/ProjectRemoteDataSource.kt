package com.connectcrew.data.datasource.project.remote

import com.connectcrew.data.model.project.RequestRecruitStatus
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity

internal interface ProjectRemoteDataSource {

    suspend fun getProjectInfo(): ProjectInfoContainerEntity

    suspend fun getProjectFeeds(
        lastId: Long?,
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

    suspend fun getProjectFeedDetail(projectId: Long): ProjectFeedDetailEntity

    suspend fun setProjectLike(projectId: Long): ProjectFeedLikeInfoEntity

    suspend fun setProjectEnrollment(projectId: Long, part: String, message: String)

    suspend fun createProjectFeed(
        title: String,
        region: String,
        isOnline: Boolean,
        state: String,
        careerMin: String,
        careerMax: String,
        leaderPart: String,
        category: List<String>,
        goal: String,
        introduction: String,
        recruits: List<RequestRecruitStatus>,
        skills: List<String>,
        bannerImageUrls: List<String>
    ): Long

    suspend fun updateProjectFeed(
        projectId: Long,
        title: String,
        region: String,
        isOnline: Boolean,
        state: String,
        careerMin: String,
        careerMax: String,
        leaderPart: String,
        category: List<String>,
        goal: String,
        introduction: String,
        recruits: List<RequestRecruitStatus>,
        skills: List<String>,
        bannerImageUrls: List<String>,
        removeBannerImageUrls: List<String>
    ): Long

    suspend fun createProjectReport(projectId: Long, reportReason: String)
}