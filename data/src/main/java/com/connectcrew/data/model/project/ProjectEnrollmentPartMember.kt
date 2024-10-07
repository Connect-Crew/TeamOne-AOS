package com.connectcrew.data.model.project

import com.connectcrew.data.model.user.User
import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.util.convertToDateTime
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
internal data class ProjectEnrollmentPartMember(
    @Json(name = "id")
    val applyId: Int,
    @Json(name = "user")
    val user: User,
    @Json(name = "part")
    val part: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "contact")
    val contact: String,
    @Json(name = "state")
    val state: String,
    @Json(name = "leaderMessage")
    val leaderMessage: String?,
    @Json(name = "leaderResponseAt")
    val enrollmentAt: String? = null,
)

internal fun ProjectEnrollmentPartMember.asEntity(): ProjectEnrollmentPartMemberEntity {
    return ProjectEnrollmentPartMemberEntity(
        applyId = applyId,
        user = user.asEntity(),
        part = part,
        message = message,
        contact = contact,
        state = state,
        leaderMessage = leaderMessage ?: "",
        enrollmentAt = enrollmentAt.convertToDateTime() ?: ZonedDateTime.now().toString()
    )
}