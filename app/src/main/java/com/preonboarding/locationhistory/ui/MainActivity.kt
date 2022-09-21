package com.preonboarding.locationhistory.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val permissionLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        }
    private val permissionGPSLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setUpMapView()

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
        } else {
            rejectPermissionCase()
        }
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
        } else {
            permissionLocationLauncher.launch(
                REQUIRED_PERMISSIONS
            )
        }
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