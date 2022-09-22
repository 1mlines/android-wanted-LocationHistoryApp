package com.preonboarding.locationhistory.data.source.local.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.repository.TimerRepository
import com.preonboarding.locationhistory.data.source.local.worker.LocationWorker
import com.preonboarding.locationhistory.di.HiltBroadCastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/21
 */
@AndroidEntryPoint
class AlarmReceiver : HiltBroadCastReceiver() {

    @Inject lateinit var timerRepository: TimerRepository
    companion object {
        const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context != null) {
            when (intent?.action) {
                context.getString(R.string.setting_intent) -> {
                    createAlarm(context)
                    WorkManager.getInstance(context).enqueue(
                        OneTimeWorkRequestBuilder<LocationWorker>().build()
                    )
                }
                context.getString(R.string.setting_boot_intent) -> {
                    createAlarm(context)
                }
            }
        }
    }

    private fun createAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.setting_intent)
        var existPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager?.cancel(existPendingIntent)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
        CoroutineScope(Dispatchers.IO).launch {
            timerRepository.getDuration().collect { time ->
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    alarmManager?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + time * 1000 * 60,
                        pendingIntent
                    )
                } else {
                    alarmManager?.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + time * 1000 * 60,
                        pendingIntent
                    )
                }
            }
        }
    }
}
