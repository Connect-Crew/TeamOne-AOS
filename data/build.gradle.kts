import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_URL", localProperties["DEV_API_URL"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_ID_DATA", localProperties["GOOGLE_CLIENT_ID_DATA"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_SECRET_DATA", localProperties["GOOGLE_CLIENT_SECRET_DATA"].toString())
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField("String", "API_URL", localProperties["RELEASE_API_URL"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_ID_DATA", localProperties["GOOGLE_CLIENT_ID_DATA"].toString())
            buildConfigField("String", "GOOGLE_CLIENT_SECRET_DATA", localProperties["GOOGLE_CLIENT_SECRET_DATA"].toString())
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}