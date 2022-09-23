package com.preonboarding.locationhistory.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.ui.dialog.HistoryDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var mCurrentLocation: Location
    private lateinit var currentPosition: LatLng
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var location: Location
    private val permissionLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        }
    private val permissionGPSLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    // 위치 정보 변경 이벤트
    private var changeLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList: List<Location> = locationResult.locations
            if (locationList.isNotEmpty()) {
                location = locationList[locationList.size - 1]
                currentPosition = LatLng(location.latitude, location.longitude)
                val markerTitle = getCurrentAddress(currentPosition)
                val markerSnippet = ("위도: ${location.latitude} 경도: ${location.longitude}")

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet)
                mCurrentLocation = location
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setUpMapView()
        getHistoryDialog()
    }

    private fun getHistoryDialog() {
        val historyButton = findViewById<Button>(R.id.button)

        historyButton.setOnClickListener {
            HistoryDialog().show(
                supportFragmentManager, "HistoryDialog"
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                changeLocationCallback,
                null
            )
            if (::map.isInitialized) map.isMyLocationEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(changeLocationCallback)
    }

    private fun getCurrentAddress(latlng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()
        runCatching {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
        }.getOrElse {
            when (it) {
                is IOException -> Toast.makeText(
                    this,
                    getString(R.string.main_not_allowed_location_service),
                    Toast.LENGTH_SHORT
                ).show()
                is IllegalArgumentException -> Toast.makeText(
                    this,
                    getString(R.string.main_error_gps),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        if (addresses.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.main_unknown_address),
                Toast.LENGTH_LONG
            )
                .show()
            return getString(R.string.main_unknown_address)
        }
        val address = addresses.first()
        return address.getAddressLine(0).toString()

    }


    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    private fun setUpMapView() {
        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_MS
            priority = Priority.PRIORITY_HIGH_ACCURACY
            fastestInterval = FASTEST_UPDATE_INTERVAL_MS
            maxWaitTime = MAX_WAIT_TIME
        }
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setDefaultLocation()
        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(
            this,
            ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
            return
        }
        rejectPermissionCase()

        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun rejectPermissionCase() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                REQUIRED_PERMISSIONS[0]
            )
        ) {
            Snackbar.make(
                binding.root, getString(R.string.main_check_location_permission_title),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(R.string.main_confirm)) {
                permissionLocationLauncher.launch(
                    REQUIRED_PERMISSIONS
                )
            }.show()
            return
        }
        permissionLocationLauncher.launch(
            REQUIRED_PERMISSIONS
        )
    }

    private fun startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            changeLocationCallback,
            Looper.myLooper()
        )
        if (checkPermission()) map.isMyLocationEnabled = true
    }

    private fun checkPermission(): Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.main_dialog_gps_permission_title))
        builder.setMessage(
            getString(R.string.main_dialog_gps_permission_content)
        )
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.main_dialog_gps_permission_positive)) { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            permissionGPSLauncher.launch(callGPSSettingIntent)
        }
        builder.setNegativeButton(
            getString(R.string.main_dialog_gps_permission_negative)
        ) { dialog, _ ->
            Toast.makeText(
                this,
                getString(R.string.main_toast_warning_user_gps_message),
                Toast.LENGTH_SHORT
            ).show()
            dialog.cancel()
        }
        builder.create().show()
    }

    private fun setCurrentLocation(
        location: Location,
        markerTitle: String,
        markerSnippet: String
    ) {
        currentMarker?.remove()
        val currentLatLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        currentMarker = map.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        map.moveCamera(cameraUpdate)
    }

    private fun setDefaultLocation() {
        val defaultLocation = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        currentMarker?.remove()
        val markerOptions = MarkerOptions()
        markerOptions.position(defaultLocation)
        markerOptions.title(getString(R.string.main_default_marker_title))
        markerOptions.snippet(getString(R.string.main_default_marker_snippet))
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        currentMarker = map.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f)
        map.moveCamera(cameraUpdate)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.lifecycleOwner = this
    }

    companion object {
        private const val DEFAULT_LATITUDE = 37.56
        private const val DEFAULT_LONGITUDE = 126.97
        private const val UPDATE_INTERVAL_MS = 1000L // 1초
        private const val FASTEST_UPDATE_INTERVAL_MS = 500L // 0.5초
        private const val MAX_WAIT_TIME = 2000L
        private val REQUIRED_PERMISSIONS = arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    }
}