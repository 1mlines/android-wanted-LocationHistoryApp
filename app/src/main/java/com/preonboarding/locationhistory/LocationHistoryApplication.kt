package com.preonboarding.locationhistory

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LocationHistoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}