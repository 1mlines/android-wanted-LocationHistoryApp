package com.preonboarding.locationhistory.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.preonboarding.locationhistory.common.Constants
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_KEY

object PreferencesUtil {

    lateinit var sharedPreferences: SharedPreferences

    //saveHistoryPeriod 초기값 15
    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            Constants.SHARED_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val initSaveHistoryPeriod: Int = sharedPreferences.getInt(SAVE_HISTORY_PERIOD_KEY, 0)
        if (initSaveHistoryPeriod == 0) {
            sharedPreferences.edit().apply {
                putInt(SAVE_HISTORY_PERIOD_KEY, Constants.SAVE_HISTORY_PERIOD_MIN)
                apply()
            }
        }
    }

    fun setSaveHistoryPeriod(period: Int) {
        sharedPreferences.edit().apply {
            putInt(SAVE_HISTORY_PERIOD_KEY, period)
            apply()
        }
    }

    fun getSaveHistoryPeriod(): Int = sharedPreferences.getInt(
        SAVE_HISTORY_PERIOD_KEY,
        0
    )

}