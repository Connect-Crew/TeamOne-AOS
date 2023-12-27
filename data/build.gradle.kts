import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.connectcrew.teamone.convention.TeamOneConfig

plugins {
    id("teamone.android.library")
    id("teamone.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    val localProperties = gradleLocalProperties(rootDir)

    defaultConfig {
        namespace = "com.connectcrew.data"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "VERSION_NAME", "\"${TeamOneConfig.versionName}\"")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_URL", localProperties["DEV_API_URL"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_ID", localProperties["GOOGLE_CLIENT_ID"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_SECRET", localProperties["GOOGLE_CLIENT_SECRET"].toString())
        }

        getByName("release") {
            buildConfigField("String", "API_URL", localProperties["RELEASE_API_URL"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_ID", localProperties["GOOGLE_CLIENT_ID"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_SECRET", localProperties["GOOGLE_CLIENT_SECRET"].toString())
        }
    }
}

dependencies {
    implementation(project(":domain"))

    // AndroidX Room
    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)

    // AndroidX DataStore
    implementation(libs.androidx.dataStore)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Retrofit
    implementation(libs.bundles.retrofit)

    // Moshi
    implementation(libs.moshi.core)
    implementation(libs.moshi.adapter)
    ksp(libs.moshi.codegen)

    // log tracker
    api(libs.timber)
    // file compressor
    api(libs.compressor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}