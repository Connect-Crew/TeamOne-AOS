package com.connectcrew.data.service

import com.connectcrew.data.model.project.ProjectFeed
import com.connectcrew.data.model.project.ProjectFeedDetail
import com.connectcrew.data.model.project.ProjectFeedLikeInfo
import com.connectcrew.data.model.project.ProjectInfoContainer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProjectApi {

    @GET("project/")
    suspend fun getProjectInfo(): ProjectInfoContainer

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

    @GET("project/{projectId}")
    suspend fun getProjectFeedDetail(
        @Header("Authorization") accessToken: String?,
        @Path("projectId") projectId: Int
    ): ProjectFeedDetail

    @POST("project/favorite")
    suspend fun setProjectLike(
        @Header("Authorization") accessToken: String?,
        @Body params: Any
    ): ProjectFeedLikeInfo

    @POST("project/apply")
    suspend fun setProjectEnrollment(
        @Header("Authorization") accessToken: String?,
        @Body params: Any
    )

    @POST("project/report")
    suspend fun createProjectReport(
        @Header("Authorization") accessToken: String?,
        @Body params: Any
    )
}