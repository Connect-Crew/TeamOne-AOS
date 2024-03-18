package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.KickReasonEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class KickReason(
    val type: String,
    val reason: String
) : Parcelable

fun KickReasonEntity.asItem(): KickReason {
    return KickReason(
        type = type,
        reason = reason
    )
}

fun KickReason.asEntity(): KickReasonEntity {
    return KickReasonEntity(
        type = type,
        reason = reason
    )
}