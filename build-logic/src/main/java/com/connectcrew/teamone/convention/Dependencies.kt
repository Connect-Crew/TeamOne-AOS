package com.connectcrew.teamone.convention

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? {
    return add("implementation", dependencyNotation)
}

fun DependencyHandler.ksp(dependencyNotation: Any): Dependency? {
    return add("ksp", dependencyNotation)
}