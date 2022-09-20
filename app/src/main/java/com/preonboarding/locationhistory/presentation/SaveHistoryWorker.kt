package com.preonboarding.locationhistory.presentation

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SaveHistoryWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        getHistory()
        saveHistory()
        return Result.success()
    }

    private fun getHistory() {
        /*todo
        * 현 위치 받아오기
        * 현재 시각 받아오기
        * */
    }

    private fun saveHistory() {
        /*todo
        * room saveHistory
        * */
    }

}