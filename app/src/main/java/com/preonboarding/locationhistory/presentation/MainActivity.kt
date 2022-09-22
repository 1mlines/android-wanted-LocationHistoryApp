package com.preonboarding.locationhistory.presentation

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.common.Constants
import com.preonboarding.locationhistory.common.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_KEY
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MAX
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MIN
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.databinding.DialogSaveHistorySettingsBinding
import com.preonboarding.locationhistory.util.AnimationUtil.shakeAnimation
import com.preonboarding.locationhistory.util.PreferencesUtil
import timber.log.Timber

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestMultiplePermissions : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var granted: Boolean = true
            permissions.entries.forEach {
                if (!it.value) {
                    granted = false
                }
            }
            if (granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    backgroundLocationPermission(222)
                } else {
                   Log.i("api 10","FDFDFFFF")
                }
            } else {
                Toast.makeText(this, "서비스를 사용하시려면 위치 추적이 허용되어야 합니다.,", Toast.LENGTH_LONG)
                    .show()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // permission Check
        checkLocationPermission()
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initMap()
        bindViews()
        registerOnSharedPreferenceChangeListener()
    }

    fun checkLocationPermission(){
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    // Android 11 이상 - BackgroundPermission Check
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun backgroundLocationPermission(backgroundLocationRequestCode: Int) {
        if (checkPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
            return
        }
        AlertDialog.Builder(this)
            .setTitle("백그라운드 위치 사용이 필요합니다.")
            .setMessage("원활한 서비스 제공을 위해 위치 권한을 항상 허용으로 설정해주세요. ")
            .setPositiveButton("확인") { _,_ ->
                // this request will take user to Application's Setting page
                requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), backgroundLocationRequestCode)
                openAppSettings(this)
            }
            .setNegativeButton("취소") { dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun checkPermissionGranted(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun openAppSettings(activity: Activity){
        val intent : Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", activity.packageName, null)
        }
        ContextCompat.startActivity(activity, intent, Bundle())

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
            showSettingDialog()
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

        checkLocationPermission()

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

    /*
    * sharedPreferences
    * */

    private fun registerOnSharedPreferenceChangeListener() {
        PreferencesUtil.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (it == SAVE_HISTORY_PERIOD_KEY) {
                //TODO work 실행
                Timber.d("period: ${PreferencesUtil.getSaveHistoryPeriod()}")
            }
        }
    }

    /*
    * settings dialog
    * */

    private fun showSettingDialog() {
        Dialog(this).apply {
            val dialogBinding: DialogSaveHistorySettingsBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(this@MainActivity),
                    R.layout.dialog_save_history_settings,
                    binding.root,
                    false
                )
            setContentView(dialogBinding.root)
            show()

            dialogBinding.cancelButton.setOnClickListener {
                dismiss()
            }

            dialogBinding.confirmButton.setOnClickListener {
                val periodText = dialogBinding.saveHistoryPeriodEditText.text
                val isCorrectPeriod: Boolean = saveHistoryPeriodValidationCheck(
                    periodText.toString()
                )
                when (isCorrectPeriod) {
                    true -> {
                        PreferencesUtil.setSaveHistoryPeriod(periodText.toString().toInt())
                        dismiss()
                    }
                    else -> {
                        showValidationWarning(dialogBinding.saveHistoryPeriodWarningTextView)
                    }
                }
            }
        }

    }

    private fun saveHistoryPeriodValidationCheck(period: String): Boolean {
        return try {
            when (period.toInt()) {
                in SAVE_HISTORY_PERIOD_MIN..SAVE_HISTORY_PERIOD_MAX -> {
                    true
                }
                else -> {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun showValidationWarning(view: View) {
        view.visibility = View.VISIBLE
        view.startAnimation(shakeAnimation(view.context))
    }

}