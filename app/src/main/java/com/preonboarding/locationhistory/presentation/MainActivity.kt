package com.preonboarding.locationhistory.presentation

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.common.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_KEY
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MAX
import com.preonboarding.locationhistory.common.Constants.SAVE_HISTORY_PERIOD_MIN
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.data.*
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.databinding.DialogAddressBinding
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.databinding.DialogSaveHistorySettingsBinding
import com.preonboarding.locationhistory.util.AnimationUtil.shakeAnimation
import com.preonboarding.locationhistory.util.PreferencesUtil
import com.preonboarding.locationhistory.util.WorkMangerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment
    private lateinit var naverMap: NaverMap
    private val workManagerUtil: WorkMangerUtil by lazy { WorkMangerUtil(this) }
    private lateinit var historyBinding: DialogHistoryBinding
    private lateinit var settingDay: String
    private lateinit var adapter: HistoryDialogAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[HistoryDialogViewModel::class.java]
    }
    private lateinit var db: HistoryDB

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private val requestMultiplePermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var granted: Boolean = true
            permissions.entries.forEach {
                if (!it.value) {
                    granted = false
                }
            }
            if (granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationPermission(222)
                }
            } else {
                Toast.makeText(this, "서비스를 사용하시려면 위치 추적이 허용되어야 합니다.,", Toast.LENGTH_LONG)
                    .show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // permission Check
        checkLocationPermission()

        initMap()
        bindViews()
        registerOnSharedPreferenceChangeListener()
        workManagerUtil.startSaveHistoryWork()
    }

    private fun checkLocationPermission() {
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
        if (checkPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            return
        }
        AlertDialog.Builder(this)
            .setTitle("백그라운드 위치 사용이 필요합니다.")
            .setMessage("원활한 서비스 제공을 위해 위치 권한을 항상 허용으로 설정해주세요. ")
            .setPositiveButton("확인") { _, _ ->
                // this request will take user to Application's Setting page
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundLocationRequestCode
                )
                openAppSettings(this)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun checkPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openAppSettings(activity: Activity) {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", activity.packageName, null)
        }
        ContextCompat.startActivity(activity, intent, Bundle())
    }

    override fun onRestart() {
        super.onRestart()

        Timber.e("RESTART")
        initMap()
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            || checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            return
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Toast.makeText(this, "서비스를 이용하시려면 백그라운드 사용 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "서비스를 이용하시려면 위치 사용 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
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
        Timber.e("InitMap")
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.naverMapFragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.naverMapFragment, it).commit()
                }

        mapFragment.getMapAsync(this)
    }


    private fun bindViews() = with(binding) {
        addressButton.setOnClickListener {
            val inflater = LayoutInflater.from(this@MainActivity)
            val binding = DialogAddressBinding.inflate(inflater, null, false)

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setView(binding.root)

            val address = convertLocationToAddress(37.336631394791, 127.08717133355)
            val dialog = builder.create()

            binding.addressTextView.text = address

            binding.negativeTextButton.setOnClickListener {
                dialog.dismiss()
            }

            binding.positiveTextButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        historyButton.setOnClickListener {
            getHistoryDialog()
        }

        settingsButton.setOnClickListener {
           // showSettingDialog()
        }
    }


    //히스토리 다이얼로그를 띄우고 리사이클러뷰 생성
    private fun getHistoryDialog() {
        historyBinding = DialogHistoryBinding.inflate(layoutInflater)
        val historyDialog =
            AlertDialog.Builder(this).setView(historyBinding.root).create()
        historyDialog.setCanceledOnTouchOutside(true)

        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val today = dateFormat.format(System.currentTimeMillis())

        if (settingDay == today) {
            historyBinding.dialogDatepickerTextView.text = today
            setHistory(today)

        } else {
            historyBinding.dialogDatepickerTextView.text = settingDay
            setHistory(settingDay)

        }

        historyBinding.dialogCancelButton.setOnClickListener {
            historyDialog.dismiss()
        }

        historyBinding.dialogConfirmButton.setOnClickListener {
            setHistory(settingDay)
            Toast.makeText(this, "날짜 변경 완료!", Toast.LENGTH_SHORT).show()
            historyDialog.dismiss()
        }
        historyBinding.dialogDatepickerTextView.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val dateString = "${year}.${month + 1}.$dayOfMonth"
                    historyBinding.dialogDatepickerTextView.text = dateString
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
        historyBinding.dialogRecyclerView.adapter = adapter
        /*
        viewModel.getHistory(today).observe(this) {
            adapter.submitList(it)
            pinMap(it)
            // 시간 보내줄때 유형 맞는지 확인 해야함!
        }
         */
    }


    //지도에 좌표 찍기
    private fun pinMap(historyList: List<History>) {
        lifecycleScope.launch {
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
        Timber.e("ONMAPREADY")

        naverMap.locationSource = locationSource

        setMapUiSettings()

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

    private fun setMapUiSettings() {
        naverMap.uiSettings.apply {
            isLocationButtonEnabled = true
            isTiltGesturesEnabled = true
            isScaleBarEnabled = false
            isCompassEnabled = false
        }
    }

    private fun convertLocationToAddress(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(this, Locale.KOREA)
        val address: ArrayList<Address>

        var result = "결과가 없습니다."

        try {
            address = geoCoder.getFromLocation(latitude, longitude, 1) as ArrayList<Address>
            if (address.size > 0) {
                val currentLocationAddress = address[0].getAddressLine(0).toString()
                result = currentLocationAddress
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
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
                workManagerUtil.startSaveHistoryWork()
                Timber.d("abcabc: ${PreferencesUtil.getSaveHistoryPeriod()}")
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
                    null,
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