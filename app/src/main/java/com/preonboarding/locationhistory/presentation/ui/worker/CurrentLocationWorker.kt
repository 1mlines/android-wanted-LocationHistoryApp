package com.preonboarding.locationhistory.presentation.ui.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.sql.Timestamp

@HiltWorker
class CurrentLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
    private val locationRepository: LocationRepository
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {

        lateinit var result: Result

        coroutineScope {
            withContext(Dispatchers.IO) {
                if (checkPermission()) {
                    val manager: LocationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    val currentLocation: android.location.Location? =
                        manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    val latitude = currentLocation?.latitude
                    val longitude = currentLocation?.longitude
                    val currentTimestamp = Timestamp(System.currentTimeMillis()).time

                    val location = if (latitude != null && longitude != null) {
                        Location(0, latitude.toFloat(), longitude.toFloat(), currentTimestamp)
                    } else {
                        Location.EMPTY
                    }

                    insertLocation(location)

                    result = Result.success()
                } else {
                    result = Result.failure()
                }
            }
        }

        return result
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun insertLocation(location: Location) {
        locationRepository.insertLocation(location)
    }
}
