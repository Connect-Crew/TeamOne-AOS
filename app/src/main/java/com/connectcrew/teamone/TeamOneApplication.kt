package com.connectcrew.teamone

import android.app.Application
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
open class TeamOneApplication : Application() {

    @Inject
    lateinit var signViewModelDelegate: SignViewModelDelegate
}