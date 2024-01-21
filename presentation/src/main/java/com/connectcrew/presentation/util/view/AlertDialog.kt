package com.connectcrew.presentation.util.view

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.connectcrew.presentation.R
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.google.android.material.imageview.ShapeableImageView

fun createAlert(context: Context, isCancelable: Boolean = true): AlertDialog {
    return AlertDialog.Builder(context).setCancelable(isCancelable).create()
}

fun AlertDialog.dialogViewBuilder(
    title: String,
    descriptionText: String,
    positiveButtonText: String = context.getString(R.string.common_confirm),
    negativeButtonText: String = context.getString(R.string.common_cancel),
    @ColorRes strokeColor: Int = R.color.color_00aee4,
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
                strokeColor = strokeColor,
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
    @StringRes descriptionRes: Int,
    @StringRes positiveButtonTextRes: Int = R.string.common_confirm,
    @StringRes negativeButtonTextRes: Int = R.string.common_cancel,
    @ColorRes strokeColor: Int = R.color.color_00aee4,
    @ColorRes iconTint: Int = R.color.color_00aee4,
    @DrawableRes iconDrawableRes: Int = R.drawable.ic_check,
    isNegativeButtonVisible: Boolean = true,
    onClickNegativeButton: (Unit) -> Unit = {},
    onClickPositiveButton: (Unit) -> Unit = {},
): AlertDialog {
    return this.apply {
        setView(
            setDialogView(
                alertDialog = this,
                title = context.getString(titleRes),
                description = context.getString(descriptionRes),
                positiveButtonText = context.getString(positiveButtonTextRes),
                negativeButtonText = context.getString(negativeButtonTextRes),
                strokeColor = strokeColor,
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
    val dialogLayout = alertDialog.layoutInflater.inflate(R.layout.dialog_common_alert, null)

    return dialogLayout.apply {
        // Icon Round Stroke
        findViewById<ShapeableImageView>(R.id.siv_bg_confirm).setStrokeColorResource(strokeColor)

        // Icon
        findViewById<ImageView>(R.id.iv_confirm).apply {
            setImageDrawable(ContextCompat.getDrawable(context, iconDrawableRes))
            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, iconTint))
        }

        // Title
        findViewById<TextView>(R.id.tv_title).text = title

        // Description
        findViewById<TextView>(R.id.tv_description).text = description

        // NegativeButton
        findViewById<Button>(R.id.btn_cancel).apply {
            text = negativeButtonText
            isVisible = isNegativeButtonVisible
            setOnSingleClickListener {
                alertDialog.dismiss()
                onClickNegativeButton(Unit)
            }
        }

        // PositiveButton
        findViewById<Button>(R.id.btn_confirm).apply {
            text = positiveButtonText
            setOnSingleClickListener {
                alertDialog.dismiss()
                onClickPositiveButton(Unit)
            }
        }
    }
}