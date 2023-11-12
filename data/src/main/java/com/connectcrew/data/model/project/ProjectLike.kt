package com.connectcrew.data.model.project

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectLike(
    @Json(name = "value")
    val isLike: Boolean
)