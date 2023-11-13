package com.connectcrew.data.datasource.project.remote

import com.connectcrew.data.model.project.ProjectFeed
import com.connectcrew.data.model.project.asEntity
import com.connectcrew.data.service.ProjectApi
import com.connectcrew.data.util.convertToAccessToken
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
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

    override suspend fun setProjectLike(accessToken: String?, projectId: Int): Boolean {
        return try {
            projectApi.setProjectLike(
                accessToken = accessToken.convertToAccessToken(),
                params = mapOf(KEY_PROJECT_ID to projectId)
            ).isLike
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "projectId"
    }
}