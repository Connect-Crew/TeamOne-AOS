package com.connectcrew.presentation.util

import androidx.annotation.StringRes

sealed class MessageType {

    data class ResourceType(@StringRes val message: Int) : MessageType()

    data class ValueType(val message: String) : MessageType()
}