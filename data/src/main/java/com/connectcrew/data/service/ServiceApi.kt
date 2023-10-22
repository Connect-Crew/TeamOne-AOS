package com.connectcrew.data.service

import retrofit2.http.GET

internal interface ServiceApi {

    @GET("test/test")
    suspend fun getTestInfo()
}