package com.connectcrew.data.datasource.project

import com.connectcrew.data.datasource.project.remote.ProjectRemoteDataSource
import com.connectcrew.data.model.project.asExternalModel
import com.connectcrew.domain.usecase.project.ProjectRepository
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.RequestRecruitStatusEntity
import javax.inject.Inject

internal class ProjectRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {

    override suspend fun getProjectInfo(): ProjectInfoContainerEntity {
        return remoteDataSource.getProjectInfo()
    }

    override suspend fun gerProjectEnrollmentMembers(projectId: Long): List<ProjectEnrollmentMemberEntity> {
        return remoteDataSource.gerProjectEnrollmentMembers(projectId)
    }

    override suspend fun getProjectEnrollmentPartMembers(projectId: Long, partKey: String): List<ProjectEnrollmentPartMemberEntity> {
        return remoteDataSource.getProjectEnrollmentPartMembers(projectId, partKey)
    }

    override suspend fun getProjectFeeds(
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
    ): List<ProjectFeedEntity> {
        return remoteDataSource.getProjectFeeds(lastId, loadSize, goal, career, region, isOnline, part, skills, states, category)
    }

    override suspend fun getProjectFeedDetail(projectId: Long): ProjectFeedDetailEntity {
        return remoteDataSource.getProjectFeedDetail(projectId)
    }

    override suspend fun setProjectLike(projectId: Long): ProjectFeedLikeInfoEntity {
        return remoteDataSource.setProjectLike(projectId)
    }

    override suspend fun setProjectEnrollment(projectId: Long, part: String, message: String, contact: String) {
        return remoteDataSource.setProjectEnrollment(projectId, part, message, contact)
    }

    override suspend fun setProjectEnrollmentPartMember(applyId: Int, isPassed: Boolean, leaderMessage: String?) {
        return remoteDataSource.setProjectEnrollmentPartMember(applyId, isPassed, leaderMessage)
    }

    override suspend fun createProjectFeed(
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
    ): Long {
        return remoteDataSource.createProjectFeed(
            title = title,
            region = region,
            isOnline = isOnline,
            state = state,
            careerMin = careerMin,
            careerMax = careerMax,
            leaderPart = leaderPart,
            category = category,
            goal = goal,
            introduction = introduction,
            recruits = recruits.map(RequestRecruitStatusEntity::asExternalModel),
            skills = skills,
            bannerImageUrls = bannerImageUrls
        )
    }

    override suspend fun updateProjectFeed(
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
        removeBannerImageUrls: List<String>
    ): Long {
        return remoteDataSource.updateProjectFeed(
            projectId = projectId,
            title = title,
            region = region,
            isOnline = isOnline,
            state = state,
            careerMin = careerMin,
            careerMax = careerMax,
            leaderPart = leaderPart,
            category = category,
            goal = goal,
            introduction = introduction,
            recruits = recruits.map(RequestRecruitStatusEntity::asExternalModel),
            skills = skills,
            bannerImageUrls = bannerImageUrls,
            removeBannerImageUrls = removeBannerImageUrls
        )
    }

    override suspend fun createProjectReport(projectId: Long, reportReason: String) {
        return remoteDataSource.createProjectReport(projectId, reportReason)
    }

    override suspend fun deleteProjectFeed(projectId: Long) {
        return remoteDataSource.deleteProjectFeed(projectId)
    }
}