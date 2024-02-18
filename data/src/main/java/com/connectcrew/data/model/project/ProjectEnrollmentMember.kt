package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectEnrollmentMember(
    @Json(name = "partDescription")
    val part: String,
    @Json(name = "partKey")
    val partKey: String,
    @Json(name = "partCategoryDescription")
    val category: String,
    @Json(name = "applies")
    val enrollCount: Int,
    @Json(name = "current")
    val currentCount: Int,
    @Json(name = "max")
    val maxCount: Int,
    @Json(name = "comment")
    val comment: String?
)

internal fun ProjectEnrollmentMember.asEntity(): ProjectEnrollmentMemberEntity {
    return ProjectEnrollmentMemberEntity(
        part = part,
        partKey = partKey,
        category = category,
        enrollCount = enrollCount,
        currentCount = currentCount,
        maxCount = maxCount,
        comment = comment
    )
}