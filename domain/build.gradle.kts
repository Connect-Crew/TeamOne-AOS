plugins {
    id("teamone.jvm.library")
    id("teamone.jvm.hilt")
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)

    // Paging
    implementation(libs.androidx.paging.common)
}