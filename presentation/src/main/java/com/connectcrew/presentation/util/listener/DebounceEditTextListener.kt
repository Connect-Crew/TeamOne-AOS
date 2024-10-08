package com.connectcrew.presentation.util.listener

import android.text.Editable
import android.text.TextWatcher
import com.connectcrew.presentation.util.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceEditTextListener(
    private var debouncePeriod: Long = 500L,
    private val scope: CoroutineScope,
    private val onDebounceEditTextChange: (String) -> Unit
) : TextWatcher {

    private var editTextChangedJob: Job? = null

    override fun beforeTextChanged(beforeText: CharSequence?, start: Int, end: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(beforeText: CharSequence?, start: Int, end: Int, count: Int) {
        if (debouncePeriod != 0L) {
            editTextChangedJob.cancelIfActive()

            editTextChangedJob = scope.launch {
                if (!beforeText.isNullOrEmpty()) {
                    delay(debouncePeriod)
                }
                onDebounceEditTextChange(beforeText.toString())
            }
        } else {
            onDebounceEditTextChange(beforeText.toString())
        }
    }
}