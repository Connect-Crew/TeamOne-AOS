package com.connectcrew.teamone.initalize

import android.content.Context
import androidx.startup.Initializer
import com.connectcrew.teamone.BuildConfig
import com.connectcrew.teamone.util.CrashlyticsMapper
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsMapper())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}