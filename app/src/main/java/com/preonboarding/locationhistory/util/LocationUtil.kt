package com.preonboarding.locationhistory.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import timber.log.Timber
import java.time.LocalDateTime

object LocationUtil {
    private var currentLocation: Location? = null

    fun getCurrentLocation(): Location? = currentLocation

    fun setCurrentLocation(context: Context) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(
                context
            )
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
        }
    }
}