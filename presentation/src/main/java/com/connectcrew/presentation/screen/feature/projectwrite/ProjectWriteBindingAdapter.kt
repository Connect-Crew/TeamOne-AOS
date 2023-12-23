package com.connectcrew.presentation.screen.feature.projectwrite

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.connectcrew.presentation.R
import com.skydoves.powerspinner.PowerSpinnerView

@BindingAdapter("progressStateCircleColor")
fun setStepTextCircleColor(textView: TextView, progressState: ProjectWriteProgressState) {
    textView.apply {
        val drawableRes = background as GradientDrawable

        when (progressState) {
            ProjectWriteProgressState.STATE_IDLE -> {
                setTextColor(context.getColor(R.color.color_9e9e9e))
                drawableRes.setStroke(1, context.getColor(R.color.color_9e9e9e))
                drawableRes.setColor(context.getColor(R.color.color_f9fafc))
            }

            ProjectWriteProgressState.STATE_PROCEEDING -> {
                setTextColor(context.getColor(android.R.color.white))
                drawableRes.setStroke(1, context.getColor(R.color.color_00aee4))
                drawableRes.setColor(context.getColor(R.color.color_00aee4))
            }

            ProjectWriteProgressState.STATE_PROGRESS_COMPLETED -> {
                setTextColor(context.getColor(R.color.color_00aee4))
                drawableRes.setStroke(1, context.getColor(R.color.color_00aee4))
                drawableRes.setColor(context.getColor(R.color.color_f9fafc))
            }
        }
    }
}

@BindingAdapter("progressStateTextColor")
fun setStepTextColor(textView: TextView, progressState: ProjectWriteProgressState) {
    textView.apply {
        when (progressState) {
            ProjectWriteProgressState.STATE_IDLE -> setTextColor(context.getColor(R.color.color_9e9e9e))
            ProjectWriteProgressState.STATE_PROCEEDING -> setTextColor(context.getColor(R.color.color_00aee4))
            ProjectWriteProgressState.STATE_PROGRESS_COMPLETED -> setTextColor(context.getColor(R.color.color_616161))
        }
    }
}

@BindingAdapter("spinnerBackgroundColor")
fun setSpinnerBackground(spinnerView: PowerSpinnerView, isReset: Boolean) {
    val drawableRes = spinnerView.background as GradientDrawable

    if (isReset) {
        spinnerView.clearSelectedItem()
    }

    spinnerView.setTextColor(spinnerView.context.getColor(if (isReset) R.color.color_9e9e9e else R.color.color_00aee4))
    drawableRes.setStroke(1, spinnerView.context.getColor(if (isReset) R.color.color_9e9e9e else R.color.color_00aee4))
    drawableRes.setColor(spinnerView.context.getColor(if (isReset) R.color.color_fdfdfd else R.color.color_f1fcff))
}