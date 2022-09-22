package com.preonboarding.locationhistory.feature.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {
    private const val PREFS_NAME = "prefs_name"
    private const val TIME_KEY = "time_key"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var setTime: String?
        get() = prefs.getString(TIME_KEY, "100")
        set(value) = prefs.edit().putString(TIME_KEY, value).apply()
}