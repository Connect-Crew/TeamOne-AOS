import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("teamone.android.library")
    id("teamone.android.hilt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")

    id("com.google.devtools.ksp")
}

android {
    val localProperties = gradleLocalProperties(rootDir)

    defaultConfig {
        namespace = "com.connectcrew.presentation"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_URL", localProperties["DEV_API_URL"].toString())

            // Google
            buildConfigField("String", "GOOGLE_CLIENT_ID", localProperties["GOOGLE_CLIENT_ID"].toString())
            // Kakao
            resValue("string", "KAKAO_API_KEY", localProperties["KAKAO_API_KEY"].toString())
            resValue("string", "KAKAO_APP_SCHEME", "kakao".plus(localProperties["KAKAO_API_KEY"].toString()))
        }

        getByName("release") {
            buildConfigField("String", "API_URL", localProperties["RELEASE_API_URL"].toString())

            // Google
            buildConfigField("String", "GOOGLE_CLIENT_ID", localProperties["GOOGLE_CLIENT_ID"].toString())
            // Kakao
            resValue("string", "KAKAO_API_KEY", localProperties["KAKAO_API_KEY"].toString())
            resValue("string", "KAKAO_APP_SCHEME", "kakao".plus(localProperties["KAKAO_API_KEY"].toString()))
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashScreen)
    implementation(libs.androidx.constraint)
    implementation(libs.androidx.startup)
    // AndroidX Paging
    implementation(libs.androidx.paging.runtime)
    // AndroidX Lifecycle
    implementation(libs.bundles.androidx.lifecycle)
    // AndroidX Navigation
    implementation(libs.bundles.androidx.navigation)
    // Android
    implementation(libs.android.material)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messageing)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Dagger2 ( DI ) Android Support
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.navigation)

    // Image loading library
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Lottie
    implementation(libs.lottie)

    // Social Login
    implementation(libs.kakao.login)
    implementation(libs.google.login)

    // log tracker
    api(libs.timber)
    api(libs.powerspinner)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}