package com.preonboarding.locationhistory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.preonboarding.locationhistory.R
import java.util.concurrent.TimeUnit

const val WORK_REPEAT_INTERVAL: Long = 15


class MainActivity : AppCompatActivity() {
    private val constraint: Constraints by lazy {
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
    private val workRequest =
        PeriodicWorkRequestBuilder<SaveHistoryWorker>(WORK_REPEAT_INTERVAL, TimeUnit.MINUTES)// 15분 반복
            .setConstraints(constraint) // 작업을 재시도 할경우에 대한 정책
            .setBackoffCriteria( // 최소 시간으로 Retry
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}