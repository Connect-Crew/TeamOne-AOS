package com.connectcrew.data.di

import com.connectcrew.data.BuildConfig
import com.connectcrew.data.token.TokenAuthenticator
import com.connectcrew.data.token.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object ServiceModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideHeaderInterceptor(): Interceptor = Interceptor { chain ->
        chain.proceed(chain.addDeviceTypeToHeader())
    }

    @TeamOneApiOkHttpCallFactory
    @Singleton
    @Provides
    fun provideApiOkHttpCallFactory(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): Call.Factory = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(headerInterceptor)
        .addInterceptor(tokenInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @NormalApiOkHttpCallFactory
    @Singleton
    @Provides
    fun provideNormalApiOkHttpCallFactory(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): Call.Factory = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @TeamOneApi
    @Singleton
    @Provides
    fun provideRetrofit(
        @TeamOneApiOkHttpCallFactory okHttpCallFactory: Call.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .callFactory(okHttpCallFactory)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .baseUrl(BuildConfig.API_URL)
            .build()
    }

    @NormalApi
    @Singleton
    @Provides
    fun provideRetrofitWithoutHeader(
        @NormalApiOkHttpCallFactory okHttpCallFactory: Call.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .callFactory(okHttpCallFactory)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .baseUrl(BuildConfig.API_URL)
            .build()
    }

    private fun Interceptor.Chain.addDeviceTypeToHeader(): Request = this.request().newBuilder()
        .addHeader("device", "aOS")
        .addHeader("version", BuildConfig.VERSION_NAME)
        .build()
}