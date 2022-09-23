package com.preonboarding.locationhistory.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.source.local.alarm.AlarmReceiver

/**
 * @Created by 김현국 2022/09/22
 */
object Alarm {
    fun create(context: Context, time: Long) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.setting_intent)
        val existPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager?.cancel(existPendingIntent)

        val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager?.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time * 1000 * 60,
                pendingIntent
            )
        } else {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time * 1000 * 60,
                pendingIntent
            )
        }
    }
}
