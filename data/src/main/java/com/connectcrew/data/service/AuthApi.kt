package com.connectcrew.data.service

import com.connectcrew.data.model.token.Token
import com.connectcrew.data.model.user.User
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthApi {

    @POST("auth/login")
    suspend fun signInForOauth(@Body params: Any): User

    @POST("auth/register")
    suspend fun signUpForOauth(@Body params: Any): User

    @POST("/auth/refresh")
    suspend fun getRefreshToken(@Header("Authorization") refreshToken: String?): Token
}