package com.connectcrew.data.di

import com.connectcrew.data.BuildConfig
import com.connectcrew.data.service.TokenApi
import com.connectcrew.data.token.TokenAuthenticator
import com.connectcrew.data.token.TokenInterceptor
import com.connectcrew.domain.preference.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object TokenServiceModule {

    @TeamOneTokenApiOkHttpCallFactory
    @Singleton
    @Provides
    fun provideApiOkHttpCallFactory(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): Call.Factory = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(headerInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideTokenApi(
        @TeamOneTokenApiOkHttpCallFactory okHttpCallFactory: Call.Factory
    ): TokenApi = Retrofit.Builder()
        .callFactory(okHttpCallFactory)
        .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
        .baseUrl(BuildConfig.API_URL)
        .build()
        .create(TokenApi::class.java)

    @Singleton
    @Provides
    internal fun provideTokenInterceptor(
        preferenceStorage: PreferenceStorage
    ): TokenInterceptor = TokenInterceptor(preferenceStorage)

    @Singleton
    @Provides
    internal fun provideTokenAuthenticator(
        preferenceStorage: PreferenceStorage,
        tokenApi: TokenApi
    ): TokenAuthenticator {
        return TokenAuthenticator(preferenceStorage, tokenApi)
    }
}