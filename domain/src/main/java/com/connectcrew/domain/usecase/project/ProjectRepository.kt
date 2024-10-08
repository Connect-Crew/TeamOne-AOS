package com.connectcrew.domain.usecase.project

import com.connectcrew.domain.usecase.project.entity.KickReasonEntity
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import com.connectcrew.domain.usecase.project.entity.RequestRecruitStatusEntity

interface ProjectRepository {

    suspend fun getProjectInfo(): ProjectInfoContainerEntity

    suspend fun gerProjectEnrollmentMembers(projectId: Long): List<ProjectEnrollmentMemberEntity>

    suspend fun getProjectEnrollmentPartMembers(projectId: Long, partKey: String): List<ProjectEnrollmentPartMemberEntity>

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

    suspend fun setProjectEnrollment(projectId: Long, part: String, message: String, contact: String)

    suspend fun setProjectEnrollmentPartMember(applyId: Int, isPassed: Boolean, leaderMessage: String?)

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
        recruits: List<RequestRecruitStatusEntity>,
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
        recruits: List<RequestRecruitStatusEntity>,
        skills: List<String>,
        bannerImageUrls: List<String>,
        removeBannerImageUrls: List<String>,
    ): Long

    suspend fun createProjectReport(projectId: Long, reportReason: String)

    suspend fun deleteProjectFeed(projectId: Long)

    suspend fun getProjectMembers(projectId: Long): List<ProjectMemberEntity>

    suspend fun kickProjectMember(projectId: Long, userId: Int, reasons: List<KickReasonEntity>): ProjectMemberEntity
}