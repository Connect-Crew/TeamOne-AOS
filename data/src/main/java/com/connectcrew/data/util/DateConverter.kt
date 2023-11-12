package com.connectcrew.data.util

internal fun String?.convertToDateTime(): String? {
    return if (this == null) null else "${this}-00:00"
}