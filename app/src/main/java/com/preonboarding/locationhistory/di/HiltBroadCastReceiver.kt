package com.preonboarding.locationhistory.di

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

/**
 * @Created by 김현국 2022/09/21
 */
abstract class HiltBroadCastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(p0: Context?, p1: Intent?) {
    }
}
