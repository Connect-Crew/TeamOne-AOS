package com.connectcrew.teamone.convention

import org.gradle.api.JavaVersion

@Suppress("unused")
object TeamOneConfig {
    const val applicationId = "com.connectcrew.teamone"

    const val minSdk = 24
    const val targetSdk = 33
    const val compileSdk = 33
    val javaCompileTarget = JavaVersion.VERSION_17

    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    const val versionName = "$versionMajor.$versionMinor.$versionPatch"
    const val versionCode = versionMajor.times(1000000) + versionMinor.times(1000) + versionPatch
}