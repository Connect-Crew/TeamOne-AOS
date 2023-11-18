package com.connectcrew.data.service

import com.connectcrew.data.model.user.User
import retrofit2.http.GET
import retrofit2.http.Header

internal interface UserApi {

    @GET("/user/myprofile")
    suspend fun getUserInfo(@Header("Authorization") accessToken: String?) : User
}