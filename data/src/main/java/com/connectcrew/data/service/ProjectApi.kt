package com.connectcrew.data.service

import com.connectcrew.data.model.project.ProjectFeed
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface ProjectApi {

    @GET("project/list")
    suspend fun getProjectFeeds(
        @Header("Authorization") accessToken: String?,
        @Query("lastId") lastId: Int?,
        @Query("size") loadSize: Int,
        @Query("goal") goal: String?,
        @Query("career") career: String?,
        @Query("region") region: String?,
        @Query("online") isOnline: Boolean?,
        @Query("part") part: String?,
        @Query("skills") skills: String?,
        @Query("states") states: String?,
        @Query("category") category: String?,
    ): List<ProjectFeed>
}