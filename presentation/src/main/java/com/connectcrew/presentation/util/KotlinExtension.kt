package com.connectcrew.presentation.util

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

fun Int?.isNull(): Boolean = this == null