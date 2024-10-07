package com.connectcrew.presentation.util

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class EditTextState {

    @Parcelize
    data object Success : EditTextState(), Parcelable

    @Parcelize
    data class Error(@StringRes val stringRes: Int) : EditTextState(), Parcelable

    @Parcelize
    data class ErrorMessage(val message: String) : EditTextState(), Parcelable

    @Parcelize
    data object Loading : EditTextState(), Parcelable

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Error -> "Error"
            is ErrorMessage -> "ErrorMessage"
            Loading -> "Loading"
        }
    }
}

val EditTextState?.Success
    get() = this is EditTextState.Success