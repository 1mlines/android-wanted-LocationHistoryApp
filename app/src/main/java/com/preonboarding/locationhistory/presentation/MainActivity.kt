package com.preonboarding.locationhistory.presentation

import android.Manifest
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
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.common.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_KEY
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MAX
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MIN
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.data.HistoryDB
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.databinding.DialogSaveHistorySettingsBinding
import com.preonboarding.locationhistory.util.AnimationUtil.shakeAnimation
import com.preonboarding.locationhistory.util.PreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var historyBinding: DialogHistoryBinding
    private lateinit var dialogViewModel: HistoryDialogViewModel
    private lateinit var settingDay: String
    private lateinit var adapter: HistoryDialogAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var db: HistoryDB

    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationSource: FusedLocationSource by lazy { FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE) }

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


    private fun openSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
        }.run(::startActivity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = HistoryDB.getDatabase(getAppContext())!!

        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val today = dateFormat.format(System.currentTimeMillis())
        settingDay = today //처음 킬때는 오늘날짜 추후에 변경시에는 세팅된 날짜로

        // permission Check
        checkLocationPermission()


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
            getHistoryDialog()
        }

        settingsButton.setOnClickListener {
            showSettingDialog()
        }
    }

    //히스토리 다이얼로그를 띄우고 리사이클러뷰 생성
    private fun getHistoryDialog() {
        historyBinding = DialogHistoryBinding.inflate(layoutInflater)
        val historyDialog = AlertDialog.Builder(this).setView(historyBinding.root).create()
        historyDialog.setCanceledOnTouchOutside(true)

        dialogViewModel = ViewModelProvider(
            this,
            HistoryDialogViewModel.Factory(instance)
        ).get(HistoryDialogViewModel::class.java)
        historyBinding.viewModel = dialogViewModel

        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val today = dateFormat.format(System.currentTimeMillis())

        if (settingDay == today) {
            historyBinding.DialogDatepickerTextView.text = today
            setHistory(today)

        } else {
            historyBinding.DialogDatepickerTextView.text = settingDay
            setHistory(settingDay)

        }



        historyBinding.DialogCancelButton.setOnClickListener {
            historyDialog.dismiss()
        }

        historyBinding.DialogConfirmButton.setOnClickListener {
            setHistory(settingDay)
            Toast.makeText(this, "날짜 변경 완료!", Toast.LENGTH_SHORT).show()
            historyDialog.dismiss()
        }
        historyBinding.DialogDatepickerTextView.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val dateString = "${year}.${month + 1}.$dayOfMonth"
                    historyBinding.DialogDatepickerTextView.text = dateString
                    settingDay = dateString
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        historyDialog.show()
    }


    //리사이클러뷰 생성
    private fun setHistory(today: String) {
        adapter = HistoryDialogAdapter()
        historyBinding.DialogRecyclerView.adapter = adapter
        dialogViewModel.getHistory(today).observe(this, androidx.lifecycle.Observer {
            adapter.setData(it)
            pinMap(it)
            // 시간 보내줄때 유형 맞는지 확인 해야함!
        })


    }


    //지도에 좌표 찍기
    private fun pinMap(historyList: List<History>) {
        CoroutineScope(Dispatchers.Main).launch {
            val load = async(Dispatchers.IO) {
                for (i in historyList) {
                    Timber.e(i.toString())
                    //TODO
                }
            }
            load.await()
        }
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        setMapUiSettings()
        getCurrentLatLng()

        trackLocationChanged()
    }

    private fun trackLocationChanged() {
        naverMap.addOnLocationChangeListener { location ->
            naverMap.locationOverlay.run {
                isVisible = true
                position = LatLng(location.latitude, location.longitude)
            }

            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(location.latitude, location.longitude)
            )

            naverMap.moveCamera(cameraUpdate)

        }
    }

    private fun getCurrentLatLng() {
        var currentLocation: Location?
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        checkLocationPermission()

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location

            if (currentLocation != null) {
                Timber.e("${currentLocation?.latitude} // ${currentLocation?.longitude}")
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