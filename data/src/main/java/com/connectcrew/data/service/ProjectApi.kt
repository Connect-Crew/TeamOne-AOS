package com.connectcrew.data.service

import com.connectcrew.data.model.project.ProjectEnrollmentMember
import com.connectcrew.data.model.project.ProjectEnrollmentPartMember
import com.connectcrew.data.model.project.ProjectFeed
import com.connectcrew.data.model.project.ProjectFeedDetail
import com.connectcrew.data.model.project.ProjectFeedLikeInfo
import com.connectcrew.data.model.project.ProjectId
import com.connectcrew.data.model.project.ProjectInfoContainer
import com.connectcrew.data.model.project.RequestRecruitStatus
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProjectApi {

    @GET("project/")
    suspend fun getProjectInfo(): ProjectInfoContainer

    @Multipart
    @POST("project/")
    suspend fun createProjectFeed(
        @Part("title") title: String,
        @Part("region") region: String,
        @Part("online") isOnline: Boolean,
        @Part("state") state: String,
        @Part("careerMin") careerMin: String,
        @Part("careerMax") careerMax: String,
        @Part("leaderParts") leaderPart: List<String>,
        @Part("category") category: List<String>,
        @Part("goal") goal: String,
        @Part("introduction") introduction: String,
        @Part("recruits") recruits: List<RequestRecruitStatus>,
        @Part("skills") skills: List<String>,
        @Part files: List<MultipartBody.Part>
    ): ProjectId

    @Multipart
    @PUT("project/{projectId}")
    suspend fun updateProjectFeed(
        @Path("projectId") projectId: Long,
        @Part("title") title: String,
        @Part("region") region: String,
        @Part("online") isOnline: Boolean,
        @Part("state") state: String,
        @Part("careerMin") careerMin: String,
        @Part("careerMax") careerMax: String,
        @Part("leaderParts") leaderPart: List<String>,
        @Part("category") category: List<String>,
        @Part("goal") goal: String,
        @Part("introduction") introduction: String,
        @Part("recruits") recruits: List<RequestRecruitStatus>,
        @Part("skills") skills: List<String>,
        @Part("removeBanners") removeBanners: List<String>,
        @Part files: List<MultipartBody.Part>
    ): ProjectId

    @GET("project/list")
    suspend fun getProjectFeeds(
        @Query("lastId") lastId: Long?,
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
    suspend fun getProjectFeedDetail(@Path("projectId") projectId: Long): ProjectFeedDetail

    @POST("project/favorite")
    suspend fun setProjectLike(@Body params: Any): ProjectFeedLikeInfo

    @POST("project/apply")
    suspend fun setProjectEnrollment(@Body params: Any)

    @GET("project/apply/{projectId}")
    suspend fun getProjectEnrollmentMembers(@Path("projectId") projectId: Long): List<ProjectEnrollmentMember>

    @GET("project/apply/{projectId}/{part}")
    suspend fun getProjectEnrollmentPartMembers(@Path("projectId") projectId: Long, @Path("part") partKey: String): List<ProjectEnrollmentPartMember>

    @POST("/project/apply/{applyId}/accept")
    suspend fun acceptProjectEnrollmentPartMember(@Path("applyId") applyId: Int, @Body params: Any)

    @POST("/project/apply/{applyId}/reject")
    suspend fun rejectProjectEnrollmentPartMember(@Path("applyId") applyId: Int, @Body params: Any)

    @POST("project/report")
    suspend fun createProjectReport(@Body params: Any)

    @DELETE("project/{projectId}/delete")
    suspend fun deleteProjectFeed(@Path("projectId") projectId: Long)
}