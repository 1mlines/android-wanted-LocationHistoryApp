package com.preonboarding.locationhistory

import android.app.Application
import android.content.Context
import com.naver.maps.map.NaverMapSdk
import com.preonboarding.locationhistory.common.Constants.NAVER_CLIENT_ID
import com.preonboarding.locationhistory.util.PreferencesUtil
import timber.log.Timber

class WantedApplication : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        initNaverMapSdk()
        initTimber()
        initSharedPreferences()
    }

    private fun initSharedPreferences() {
        PreferencesUtil.initSharedPreferences(this)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(applicationContext).client =
            NaverMapSdk.NaverCloudPlatformClient(NAVER_CLIENT_ID)
    }

    companion object {
        lateinit var instance: WantedApplication
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
}