package com.preonboarding.locationhistory.data.source.local.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.source.local.worker.LocationWorker
import timber.log.Timber

/**
 * @Created by 김현국 2022/09/21
 */
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "AlarmReceiver"
        const val id = "PRIMARY-NOTIFICATION-CHANNEL"
    }

    lateinit var notificationManager: NotificationManager
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.tag(TAG).d("알람")

        notificationManager = context?.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        createNotificationChannel()
        if (context != null) {
            createNotification(context)
        }

        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<LocationWorker>().build()
        )
    }

    private fun createNotification(context: Context) {
        val builder =
            NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alert")
                .setContentText("This is repeating alarm")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(0, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                id,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "AlarmManager Tests"
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
    }
}
