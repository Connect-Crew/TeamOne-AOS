package com.connectcrew.data.util

internal fun String?.convertToAccessToken(): String {
    return "Bearer $this"
}