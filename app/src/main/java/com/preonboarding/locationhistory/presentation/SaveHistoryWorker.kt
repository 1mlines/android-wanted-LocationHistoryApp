package com.preonboarding.locationhistory.presentation

import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.preonboarding.locationhistory.util.LocationUtil
import timber.log.Timber

class SaveHistoryWorker(tempContext: Context, params: WorkerParameters) :
    Worker(tempContext, params) {

    private val context: Context by lazy {
        tempContext
    }

    override fun doWork(): Result {
        LocationUtil.setCurrentLocation(context)

        return when (val currentLocation: Location? = LocationUtil.getCurrentLocation()) {
            null -> Result.retry()
            else -> {
                saveHistory(currentLocation)
                Result.success()
            }
        }
    }

    private fun saveHistory(currentLocation: Location?) {
        currentLocation?.let {
            Timber.d("abc ${it}")
            /*todo
           * room saveHistory
           * */
        }
    }

}