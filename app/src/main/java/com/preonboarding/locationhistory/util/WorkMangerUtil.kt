package com.preonboarding.locationhistory.util

import android.content.Context
import androidx.work.*
import com.preonboarding.locationhistory.common.Constants
import com.preonboarding.locationhistory.presentation.SaveHistoryWorker
import java.util.concurrent.TimeUnit

class WorkMangerUtil(context: Context) {

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }
    private val constraint: Constraints by lazy {
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }

    private fun getSaveHistoryWorkRequest(): PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<SaveHistoryWorker>(
            PreferencesUtil.getSaveHistoryPeriod().toLong() + 5L,// period + 5 - FLEX TIME(5) 부터 work 시작
            TimeUnit.MINUTES
        )
            .setConstraints(constraint) // 작업을 재시도 할경우에 대한 정책
            .addTag(Constants.WORK_SAVE_HISTORY)
            .setBackoffCriteria( // 최소 시간(10초)으로 Retry
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

    fun startSaveHistoryWork() {

        val saveHistoryWorkRequest: PeriodicWorkRequest = getSaveHistoryWorkRequest()

        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_SAVE_HISTORY,
            ExistingPeriodicWorkPolicy.REPLACE, //동일한 이름의 Work Replace
            saveHistoryWorkRequest
        )
    }
}