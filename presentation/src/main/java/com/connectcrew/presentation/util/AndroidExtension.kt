package com.connectcrew.presentation.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// navigation
fun NavController.safeNavigate(
    navDestination: NavDirections,
    navOptions: NavOptions? = null
) {
    val action = currentDestination?.getAction(navDestination.actionId) ?: graph.getAction(navDestination.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(navDestination, navOptions)
    }
}

// context
fun Context.tintColor(@ColorRes colorRes: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(this, colorRes))
}

fun Context.color(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

// dataBinding
fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

// EditText
fun TextInputLayout.setTextInputError(editTextState: EditTextState) {
    when (editTextState) {
        is EditTextState.Error -> {
            isErrorEnabled = true
            error = context.getString(editTextState.stringRes)
        }

        is EditTextState.ErrorMessage -> {
            isErrorEnabled = true
            error = editTextState.message
        }

        is EditTextState.Success -> {
            isErrorEnabled = false
        }

        else -> return
    }
}

// keyboard
fun View.showKeyboard(requestFocus: Boolean = false) {
    if (requestFocus) this.requestFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard(clearFocus: Boolean = false) {
    if (clearFocus) this.clearFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

// value Parse
fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).roundToInt()

// StateFlow
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}
