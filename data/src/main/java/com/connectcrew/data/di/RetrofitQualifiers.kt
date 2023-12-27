package com.connectcrew.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TeamOneApiOkHttpCallFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TeamOneTokenApiOkHttpCallFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class NormalApiOkHttpCallFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TeamOneApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class NormalApi