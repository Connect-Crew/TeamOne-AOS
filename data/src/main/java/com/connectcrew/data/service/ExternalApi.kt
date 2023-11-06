package com.connectcrew.data.service

import com.connectcrew.data.model.google.GoogleTokenInfo
import retrofit2.http.Body
import retrofit2.http.POST

/** 저희 서버가 아닌, 외부 경로 API의 경우 여기에 등록해주세요.  */
internal interface ExternalApi {

    @POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getGoogleAccessToken(@Body params: Any): GoogleTokenInfo
}