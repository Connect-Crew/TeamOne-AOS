import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.connectcrew.teamone.convention.TeamOneConfig

plugins {
    id("teamone.android.application")
    id("teamone.android.hilt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    val localProperties = gradleLocalProperties(rootDir)

    defaultConfig {
        namespace = "com.connectcrew.teamone"
        applicationId = TeamOneConfig.applicationId
        versionCode = TeamOneConfig.versionCode
        versionName = TeamOneConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = localProperties["STORE_PASSWORD"].toString()
            keyAlias = localProperties["KEY_ALIAS"].toString()
            keyPassword = localProperties["KEY_PASSWORD"].toString()
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }

        getByName("release") {
            isMinifyEnabled = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // AndroidX
    implementation(libs.androidx.startup)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    // log tracker
    api(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}