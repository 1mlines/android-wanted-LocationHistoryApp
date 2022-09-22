package com.preonboarding.locationhistory.data.source.local.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.preonboarding.locationhistory.data.repository.LocationRepository
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Created by 김현국 2022/09/20
 */

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val locationRepository: LocationRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return coroutineScope {
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Result.failure()
            } else {
                val manager: LocationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val currentLocation: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val latitude = currentLocation?.latitude
                val longitude = currentLocation?.longitude

                val now = System.currentTimeMillis()
                val date = Date(now)
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val time = simpleDateFormat.format(date)
                if (latitude != null && longitude != null) {
                    val entity = LocationEntity(
                        id = 0,
                        latitude = latitude.toFloat(),
                        longitude = longitude.toFloat(),
                        date = time
                    )
                    locationRepository.saveLocation(location = entity)
                }
                Result.success()
            }
        }
    }
}
