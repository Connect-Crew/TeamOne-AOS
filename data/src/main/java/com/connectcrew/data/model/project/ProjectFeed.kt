package com.connectcrew.data.model.project

import com.connectcrew.data.util.convertToDateTime
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
internal data class ProjectFeed(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "thumbnail")
    val thumbnail: String?,
    @Json(name = "online")
    val isOnline: Boolean,
    @Json(name = "careerMin")
    val careerMin: String,
    @Json(name = "careerMax")
    val careerMax: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "startDate")
    val startDate: String?,
    @Json(name = "endDate")
    val endDate: String?,
    @Json(name = "state")
    val state: String,
    @Json(name = "favorite")
    val likeCount: Int,
    @Json(name = "category")
    val category: List<String>,
    @Json(name = "goal")
    val goal: String,
    @Json(name = "myFavorite")
    val isLike: Boolean = false,
    @Json(name = "recruitStatus")
    val recruitStatus: List<RecruitStatus>
)

internal fun ProjectFeed.asEntity(): ProjectFeedEntity {
    return ProjectFeedEntity(
        id = id,
        title = title,
        region = region,
        thumbnail = thumbnail,
        isOnline = isOnline,
        careerMin = careerMin,
        careerMax = careerMax,
        createdAt = createdAt.convertToDateTime() ?: ZonedDateTime.now().toString(),
        startDate = startDate.convertToDateTime(),
        endDate = endDate.convertToDateTime(),
        state = state,
        likeCount = likeCount,
        category = category,
        goal = goal,
        isLike = isLike,
        recruitStatus = recruitStatus.map(RecruitStatus::asEntity),
    )
}