package com.connectcrew.data.service

import com.connectcrew.data.model.google.GoogleTokenInfo
import com.connectcrew.data.model.token.Token
import com.connectcrew.data.model.user.User
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ServiceApi {

    @POST("auth/login")
    suspend fun signInForOauth(@Body params: Any): User

    @POST("auth/register")
    suspend fun signUpForOauth(@Body params: Any): User

    @POST("/auth/refresh")
    suspend fun getRefreshToken(@Body params: Any): Token

    @POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getGoogleAccessToken(@Body params: Any): GoogleTokenInfo
}