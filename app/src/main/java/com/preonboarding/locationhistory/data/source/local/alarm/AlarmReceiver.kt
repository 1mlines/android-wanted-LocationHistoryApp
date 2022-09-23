package com.preonboarding.locationhistory.data.source.local.alarm

import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.repository.TimerRepository
import com.preonboarding.locationhistory.data.source.local.worker.LocationWorker
import com.preonboarding.locationhistory.di.HiltBroadCastReceiver
import com.preonboarding.locationhistory.util.Alarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/21
 */
@AndroidEntryPoint
class AlarmReceiver : HiltBroadCastReceiver() {

    @Inject lateinit var timerRepository: TimerRepository

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
        CoroutineScope(Dispatchers.IO).launch {
            timerRepository.getDuration().collect { time ->
                if (time != 0L) {
                    Alarm.create(context, time)
                }
            }
        }
    }
}
