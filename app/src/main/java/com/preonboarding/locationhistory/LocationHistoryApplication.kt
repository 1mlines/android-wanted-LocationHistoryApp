package com.preonboarding.locationhistory

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LocationHistoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}