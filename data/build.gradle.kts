plugins {
    id("teamone.android.library")
    id("teamone.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    defaultConfig {
        namespace = "com.connectcrew.data"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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