package com.connectcrew.data.service

import com.connectcrew.data.model.token.Token
import retrofit2.http.Header
import retrofit2.http.POST

internal interface TokenApi {

    @POST("/auth/refresh")
    suspend fun getRefreshToken(@Header("Authorization") refreshToken: String?): Token
}