package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.KickReasonEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class KickReason(
    val type: String,
    val reason: String
)

internal fun KickReason.asEntity(): KickReasonEntity {
    return KickReasonEntity(
        type = type,
        reason = reason
    )
}

internal fun KickReasonEntity.asExternalModel(): KickReason {
    return KickReason(
        type = type,
        reason = reason
    )
}