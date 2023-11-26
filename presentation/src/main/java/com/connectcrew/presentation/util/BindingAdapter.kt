package com.connectcrew.presentation.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.connectcrew.presentation.BuildConfig
import com.connectcrew.presentation.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        if (!isValidContextForGlide(view.context)) return
        Glide.with(view.context)
            .load(if (imageUrl.startsWith("http")) imageUrl else (BuildConfig.API_URL + imageUrl))
            .placeholder(R.color.color_9e9e9e)
            .error(R.color.color_9e9e9e)
            .into(view)
    }
}

@BindingAdapter("profileUrl")
fun loadProfileUrl(view: ImageView, imageUrl: String?) {
    if (!isValidContextForGlide(view.context)) return
    val profileImageUrl = imageUrl?.let { if (it.startsWith("http")) it else (BuildConfig.API_URL + it) }

    Glide.with(view.context)
        .load(profileImageUrl ?: R.drawable.ic_default_profile)
        .placeholder(R.color.color_9e9e9e)
        .error(R.color.color_9e9e9e)
        .into(view)
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

private fun isValidContextForGlide(context: Context): Boolean {
    if (context is Activity) {
        return !context.isFinishing
    }
    return true
}