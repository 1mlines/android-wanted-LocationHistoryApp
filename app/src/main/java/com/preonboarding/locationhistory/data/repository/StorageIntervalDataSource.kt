package com.preonboarding.locationhistory.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class StorageIntervalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getStorageInterval(): Long {
        return sharedPreferences.getLong("interval", 16L)
    }

    fun updateStorageInterval(interval: Long) {
        sharedPreferences.edit().putLong("interval", interval).apply()
    }
}
