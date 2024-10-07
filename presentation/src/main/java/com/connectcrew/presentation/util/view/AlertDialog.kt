package com.connectcrew.presentation.util.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogCommonAlertBinding
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.tintColor

fun createAlert(context: Context, isCancelable: Boolean = true): AlertDialog {
    return AlertDialog.Builder(context).setCancelable(isCancelable).create()
}

fun AlertDialog.dialogViewBuilder(
    title: String,
    descriptionText: String,
    positiveButtonText: String = context.getString(R.string.common_confirm),
    negativeButtonText: String = context.getString(R.string.common_cancel),
    @ColorRes strokeColor: Int? = null,
    @ColorRes iconTint: Int = R.color.color_00aee4,
    @DrawableRes iconDrawableRes: Int = R.drawable.ic_check,
    isNegativeButtonVisible: Boolean = true,
    onClickNegativeButton: (Unit) -> Unit = {},
    onClickPositiveButton: (Unit) -> Unit = {}
): AlertDialog {
    return this.apply {
        setView(
            setDialogView(
                alertDialog = this,
                title = title,
                description = descriptionText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                strokeColor = strokeColor ?: iconTint,
                iconTint = iconTint,
                iconDrawableRes = iconDrawableRes,
                isNegativeButtonVisible = isNegativeButtonVisible,
                onClickNegativeButton = onClickNegativeButton,
                onClickPositiveButton = onClickPositiveButton
            )
        )
    }
}

fun AlertDialog.dialogViewBuilder(
    @StringRes titleRes: Int,
    titleResArg: Any? = null,
    @StringRes descriptionRes: Int,
    descriptionResArg: Any? = null,
    @StringRes positiveButtonTextRes: Int = R.string.common_confirm,
    @StringRes negativeButtonTextRes: Int = R.string.common_cancel,
    @ColorRes strokeColor: Int? = null,
    @ColorRes iconTint: Int = R.color.color_00aee4,
    @DrawableRes iconDrawableRes: Int = R.drawable.ic_check,
    isNegativeButtonVisible: Boolean = true,
    onClickNegativeButton: (Unit) -> Unit = {},
    onClickPositiveButton: (Unit) -> Unit = {}
): AlertDialog {
    return this.apply {
        setView(
            setDialogView(
                alertDialog = this,
                title = if (titleResArg == null) context.getString(titleRes) else context.getString(titleRes, titleResArg),
                description = if (descriptionResArg == null) context.getString(descriptionRes) else context.getString(descriptionRes, descriptionResArg),
                positiveButtonText = context.getString(positiveButtonTextRes),
                negativeButtonText = context.getString(negativeButtonTextRes),
                strokeColor = strokeColor ?: iconTint,
                iconTint = iconTint,
                iconDrawableRes = iconDrawableRes,
                isNegativeButtonVisible = isNegativeButtonVisible,
                onClickNegativeButton = onClickNegativeButton,
                onClickPositiveButton = onClickPositiveButton
            )
        )
    }
}

private fun setDialogView(
    alertDialog: AlertDialog,
    title: String,
    description: String,
    positiveButtonText: String,
    negativeButtonText: String,
    @ColorRes strokeColor: Int,
    @ColorRes iconTint: Int,
    @DrawableRes iconDrawableRes: Int,
    isNegativeButtonVisible: Boolean = true,
    onClickNegativeButton: (Unit) -> Unit,
    onClickPositiveButton: (Unit) -> Unit
): View {

    val dialogBinding = DialogCommonAlertBinding.inflate(LayoutInflater.from(alertDialog.context))

    return dialogBinding.apply {
        // Icon Round Stroke
        sivBgConfirm.setStrokeColorResource(strokeColor)

        // Icon
        ivConfirm.apply {
            setImageDrawable(ContextCompat.getDrawable(context, iconDrawableRes))
            imageTintList = context.tintColor(iconTint)
        }

        // Title
        tvTitle.text = title

        // Description
        tvDescription.text = description

        // NegativeButton
        btnCancel.apply {
            text = negativeButtonText
            isVisible = isNegativeButtonVisible
            setOnSingleClickListener {
                alertDialog.dismiss()
                onClickNegativeButton(Unit)
            }
        }

        // PositiveButton
        btnConfirm.apply {
            text = positiveButtonText
            setOnSingleClickListener {
                alertDialog.dismiss()
                onClickPositiveButton(Unit)
            }
        }
    }.root
}