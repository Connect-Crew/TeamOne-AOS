package com.connectcrew.data.datasource.project.remote

import com.connectcrew.data.model.project.ProjectFeed
import com.connectcrew.data.model.project.asEntity
import com.connectcrew.data.service.ProjectApi
import com.connectcrew.data.util.convertToAccessToken
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import javax.inject.Inject

internal class ProjectRemoteDataSourceImpl @Inject constructor(
    private val projectApi: ProjectApi
) : ProjectRemoteDataSource {

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
        return try {
            projectApi.getProjectFeeds(
                accessToken = accessToken.convertToAccessToken(),
                lastId = lastId,
                loadSize = loadSize,
                goal = goal,
                career = career,
                region = region,
                isOnline = isOnline,
                part = part,
                skills = skills,
                states = states,
                category = category
            ).map(ProjectFeed::asEntity)
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun getProjectFeedDetail(accessToken: String?, projectId: Int): ProjectFeedDetailEntity {
        return try {
            projectApi.getProjectFeedDetail(
                accessToken = accessToken.convertToAccessToken(),
                projectId = projectId
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun setProjectLike(accessToken: String?, projectId: Int): ProjectFeedLikeInfoEntity {
        return try {
            projectApi.setProjectLike(
                accessToken = accessToken.convertToAccessToken(),
                params = mapOf(KEY_PROJECT_ID to projectId)
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun setProjectEnrollment(accessToken: String?, projectId: Int, part: String, message: String) {
        return try {
            projectApi.setProjectEnrollment(
                accessToken = accessToken.convertToAccessToken(),
                params = mapOf(
                    KEY_PROJECT_ID to projectId,
                    KEY_PROJECT_ENROLLMENT_PART to part,
                    KEY_PROJECT_ENROLLMENT_REASON to message
                )
            )
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun createProjectReport(accessToken: String?, projectId: Int, reportReason: String) {
        return try {
            projectApi.createProjectReport(
                accessToken = accessToken.convertToAccessToken(),
                params = mapOf(
                    KEY_PROJECT_ID to projectId,
                    KEY_PROJECT_REPORT_REASON to reportReason
                )
            )
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "projectId"
        private const val KEY_PROJECT_ENROLLMENT_PART = "part"
        private const val KEY_PROJECT_ENROLLMENT_REASON = "message"
        private const val KEY_PROJECT_REPORT_REASON = "reason"
    }
}