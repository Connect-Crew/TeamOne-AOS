package com.connectcrew.data.model.project

import com.connectcrew.data.model.user.User
import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.util.convertToDateTime
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
internal data class ProjectFeedDetail(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "banners")
    val bannerImageUrls: List<String>,
    @Json(name = "region")
    val region: String,
    @Json(name = "online")
    val isOnline: Boolean,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "state")
    val state: String,
    @Json(name = "careerMin")
    val careerMin: String,
    @Json(name = "careerMax")
    val careerMax: String,
    @Json(name = "category")
    val category: List<String>,
    @Json(name = "goal")
    val goal: String,
    @Json(name = "introduction")
    val projectIntroduction: String,
    @Json(name = "favorite")
    val likeCount: Int,
    @Json(name = "myFavorite")
    val isLike: Boolean,
    @Json(name = "leader")
    val leader: User,
    @Json(name = "recruitStatus")
    val recruitStatus: List<RecruitStatus>,
    @Json(name = "skills")
    val skills: List<String>
)

internal fun ProjectFeedDetail.asEntity(): ProjectFeedDetailEntity {
    return ProjectFeedDetailEntity(
        id = id,
        title = title,
        bannerImageUrls = bannerImageUrls,
        region = region,
        isOnline = isOnline,
        createdAt = createdAt.convertToDateTime() ?: ZonedDateTime.now().toString(),
        state = state,
        careerMin = careerMin,
        careerMax = careerMax,
        category = category,
        goal = goal,
        projectIntroduction = projectIntroduction,
        likeCount = likeCount,
        isLike = isLike,
        leader = leader.asEntity(),
        recruitStatus = recruitStatus.map(RecruitStatus::asEntity),
        skills = skills
    )
}