package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.RecruitStatusEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class RecruitStatus(
    @Json(name = "part")
    val part: String,
    @Json(name = "partKey")
    val partKey: String,
    @Json(name = "comment")
    val comment: String,
    @Json(name = "current")
    val currentCount: Int,
    @Json(name = "max")
    val maxCount: Int,
    @Json(name = "category")
    val category: String,
    @Json(name = "applied")
    val isApplied: Boolean?
)

internal fun RecruitStatus.asEntity(): RecruitStatusEntity {
    return RecruitStatusEntity(
        part = part,
        partKey = partKey,
        comment = comment,
        currentCount = currentCount,
        maxCount = maxCount,
        category = category,
        isApplied = isApplied
    )
}