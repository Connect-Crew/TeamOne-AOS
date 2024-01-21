package com.connectcrew.presentation.model.user

import android.os.Parcelable
import com.connectcrew.domain.usecase.sign.entity.JobPartEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobPart(
    val key: String,
    val value: String,
    val category: String
) : Parcelable

fun JobPartEntity.asItem(): JobPart {
    return JobPart(
        key = key,
        value = value,
        category = category
    )
}

fun JobPart.asEntity(): JobPartEntity {
    return JobPartEntity(
        key = key,
        value = value,
        category = category
    )
}