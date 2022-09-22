package com.preonboarding.locationhistory.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.common.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.preonboarding.locationhistory.common.Constants.WORK_REPEAT_INTERVAL
import com.preonboarding.locationhistory.common.Constants.WORK_SAVE_HISTORY
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String = MainActivity::class.java.name
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { permission ->
                when {
                    permission.value -> {
                        Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show()
                    }

                    shouldShowRequestPermissionRationale(permission.key) -> {
                        Toast.makeText(this, "REQUIRE PERMISSION", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        openSettings()
                    }
                }
            }
        }

    private fun openSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
        }.run(::startActivity)
    }
    private val workManager: WorkManager by lazy { WorkManager.getInstance(this) }
    private val constraint: Constraints by lazy {
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
    private val saveHistoryWorkRequest: PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<SaveHistoryWorker>(
            WORK_REPEAT_INTERVAL,// 15분 반복
            TimeUnit.MINUTES
        )
            .setConstraints(constraint) // 작업을 재시도 할경우에 대한 정책
            .addTag(WORK_SAVE_HISTORY)
            .setBackoffCriteria( // 최소 시간(10초)으로 Retry
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // permission Check
        permissionLauncher.launch(locationPermissions)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initMap()
        bindViews()
        startSaveHistoryWork()
    }

    private fun startSaveHistoryWork() {

        workManager.enqueueUniquePeriodicWork(
            WORK_SAVE_HISTORY,
            ExistingPeriodicWorkPolicy.REPLACE,
            saveHistoryWorkRequest
        )
        workManager.getWorkInfoByIdLiveData(saveHistoryWorkRequest.id).observe(this) {
            Timber.d(TAG, "startSaveHistoryWork: ${it.state}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            naverMap.locationTrackingMode = LocationTrackingMode.Face
            return
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.naverMapFragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction()
                    .add(R.id.naverMapFragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    private fun bindViews() = with(binding) {
        addressButton.setOnClickListener {
            // TODO
        }

        historyButton.setOnClickListener {
            // TODO
        }

        settingsButton.setOnClickListener {
            // TODO
        }
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        setMapUiSettings()

        var currentLocation: Location?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location

            naverMap.locationOverlay.run {
                isVisible = true
                position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            }

            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            )

            naverMap.moveCamera(cameraUpdate)

            Timber.e("${currentLocation!!.latitude} // ${currentLocation!!.longitude}")
        }
    }

    private fun setMapUiSettings() {
        naverMap.uiSettings.apply {
            isLocationButtonEnabled = true
            isTiltGesturesEnabled = true
            isScaleBarEnabled = false
            isCompassEnabled = false
        }
    }


    override fun onDestroy() {
        workManager.cancelUniqueWork(WORK_SAVE_HISTORY)
        super.onDestroy()
    }
}