package com.connectcrew.data.datasource.project.remote

import com.connectcrew.data.model.project.KickReason
import com.connectcrew.data.model.project.ProjectEnrollmentMember
import com.connectcrew.data.model.project.ProjectEnrollmentPartMember
import com.connectcrew.data.model.project.ProjectFeed
import com.connectcrew.data.model.project.ProjectMember
import com.connectcrew.data.model.project.RequestRecruitStatus
import com.connectcrew.data.model.project.asEntity
import com.connectcrew.data.service.ProjectApi
import com.connectcrew.data.util.CompressorUtil
import com.connectcrew.data.util.FileUtil
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.net.URLEncoder
import javax.inject.Inject

internal class ProjectRemoteDataSourceImpl @Inject constructor(
    private val projectApi: ProjectApi,
    private val fileUtil: FileUtil,
    private val compressorUtil: CompressorUtil
) : ProjectRemoteDataSource {

    override suspend fun getProjectInfo(): ProjectInfoContainerEntity {
        return try {
            projectApi.getProjectInfo().asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun gerProjectEnrollmentMembers(projectId: Long): List<ProjectEnrollmentMemberEntity> {
        return try {
            projectApi.getProjectEnrollmentMembers(projectId).map(ProjectEnrollmentMember::asEntity)
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun getProjectEnrollmentPartMembers(projectId: Long, partKey: String): List<ProjectEnrollmentPartMemberEntity> {
        return try {
            projectApi.getProjectEnrollmentPartMembers(projectId, partKey).map(ProjectEnrollmentPartMember::asEntity)
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun setProjectEnrollmentPartMember(applyId: Int, isPassed: Boolean, leaderMessage: String?) {
        try {
            if (isPassed) {
                projectApi.acceptProjectEnrollmentPartMember(applyId, "")
            } else {
                projectApi.rejectProjectEnrollmentPartMember(applyId, leaderMessage ?: "")
            }
        } catch (e: Exception) {
            throw converterException(e)
        }
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
        return try {
            projectApi.getProjectFeeds(
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
            ).map(ProjectFeed::asEntity).sortedByDescending { it.id }
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun getProjectFeedDetail(projectId: Long): ProjectFeedDetailEntity {
        return try {
            projectApi.getProjectFeedDetail(projectId).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun setProjectLike(projectId: Long): ProjectFeedLikeInfoEntity {
        return try {
            projectApi.setProjectLike(
                params = mapOf(KEY_PROJECT_ID to projectId)
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun setProjectEnrollment(projectId: Long, part: String, message: String, contact: String) {
        return try {
            projectApi.setProjectEnrollment(
                params = mapOf(
                    KEY_PROJECT_ID to projectId,
                    KEY_PROJECT_ENROLLMENT_PART to part,
                    KEY_PROJECT_ENROLLMENT_REASON to message,
                    KEY_PROJECT_CONTACT to contact
                )
            )
        } catch (e: Exception) {
            throw converterException(e)
        }
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
        recruits: List<RequestRecruitStatus>,
        skills: List<String>,
        bannerImageUrls: List<String>
    ): Long {

        val partList = bannerImageUrls.map {
            val compressedFile = compressorUtil.compressFile(fileUtil.from(it))

            MultipartBody.Part.createFormData(
                name = KEY_PROJECT_BANNER,
                filename = URLEncoder.encode(compressedFile.name, Charsets.UTF_8.displayName()),
                body = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        return try {
            projectApi.createProjectFeed(
                title = title,
                region = region,
                isOnline = isOnline,
                state = state,
                careerMin = careerMin,
                careerMax = careerMax,
                leaderPart = listOf(leaderPart),
                category = category,
                goal = goal,
                introduction = introduction,
                recruits = recruits,
                skills = skills.ifEmpty { listOf("") },
                files = partList
            ).projectId
        } catch (e: Exception) {
            throw converterException(e)
        }
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
        recruits: List<RequestRecruitStatus>,
        skills: List<String>,
        bannerImageUrls: List<String>,
        removeBannerImageUrls: List<String>
    ): Long {
        return try {
            val partList = bannerImageUrls.filter { it.startsWith("content") }.map {
                val compressedFile = compressorUtil.compressFile(fileUtil.from(it))

                MultipartBody.Part.createFormData(
                    name = KEY_PROJECT_BANNER,
                    filename = URLEncoder.encode(compressedFile.name, Charsets.UTF_8.displayName()),
                    body = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            projectApi.updateProjectFeed(
                projectId = projectId,
                title = title,
                region = region,
                isOnline = isOnline,
                state = state,
                careerMin = careerMin,
                careerMax = careerMax,
                leaderPart = listOf(leaderPart),
                category = category,
                goal = goal,
                introduction = introduction,
                recruits = recruits,
                skills = skills.ifEmpty { listOf("") },
                removeBanners = removeBannerImageUrls,
                files = partList
            ).projectId
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun deleteProjectFeed(projectId: Long) {
        return try {
            projectApi.deleteProjectFeed(projectId)
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun createProjectReport(projectId: Long, reportReason: String) {
        return try {
            projectApi.createProjectReport(
                params = mapOf(
                    KEY_PROJECT_ID to projectId,
                    KEY_PROJECT_REPORT_REASON to reportReason
                )
            )
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    override suspend fun getProjectMembers(projectId: Long): List<ProjectMemberEntity> {
        return try {
            projectApi.getProjectMembers(projectId).map(ProjectMember::asEntity)
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

//    ::TODO API 수정 이후 변경
    override suspend fun kickProjectMember(projectId: Long, userId: Int, reasons: List<KickReason>): ProjectMemberEntity {
        return try {
            projectApi.kickProjectMember(
                params = mapOf(
                    "project" to projectId,
                    KEY_USER_ID to userId,
                    KEY_PROJECT_MEMBER_KICK_REASONS to reasons
                )
            ).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "projectId"
        private const val KEY_PROJECT_ENROLLMENT_PART = "part"
        private const val KEY_PROJECT_ENROLLMENT_REASON = "message"
        private const val KEY_PROJECT_REPORT_REASON = "reason"
        private const val KEY_PROJECT_BANNER = "banner"
        private const val KEY_PROJECT_CONTACT = "contact"
        private const val KEY_USER_ID = "userId"
        private const val KEY_PROJECT_MEMBER_KICK_REASONS = "reasons"
    }
}
