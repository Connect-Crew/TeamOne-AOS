package com.connectcrew.data.service

import com.connectcrew.data.model.user.User
import retrofit2.http.GET

internal interface UserApi {

    @GET("/user/myprofile")
    suspend fun getUserInfo() : User
}