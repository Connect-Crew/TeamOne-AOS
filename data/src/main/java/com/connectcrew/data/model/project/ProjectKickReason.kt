package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.KickReasonEntity
import com.connectcrew.domain.usecase.project.entity.ProjectKickReasonEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectKickReason(
    val projectId: Long,
    val userId: Int,
    val reasons: List<KickReason>
)

internal fun ProjectKickReason.asEntity(): ProjectKickReasonEntity {
    return ProjectKickReasonEntity(
        projectId = projectId,
        userId = userId,
        reasons = reasons.map(KickReason::asEntity)
    )
}

internal fun ProjectKickReasonEntity.asExternalModel(): ProjectKickReason {
    return ProjectKickReason(
        projectId = projectId,
        userId = userId,
        reasons = reasons.map(KickReasonEntity::asExternalModel)
    )
}