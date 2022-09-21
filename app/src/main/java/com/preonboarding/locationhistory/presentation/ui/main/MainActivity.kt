package com.preonboarding.locationhistory.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.presentation.custom.dialog.HistoryFragmentDialog
import com.preonboarding.locationhistory.presentation.custom.dialog.TimerFragmentDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private val mainViewModel: MainViewModel by viewModels()
    private val ACCESS_FINE_LOCATION = 1000     // Request Code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel.initCurrentDate()

        if (checkLocationService()) {
            permissionCheck()
        } else {
            Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
        }

        bindingViewModel()
        initMapView()
        initListener()
        setContentView(binding.root)
    }

    private fun bindingViewModel() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.currentDate.collect {
                Timber.tag(HistoryFragmentDialog.TAG).e("오늘 날짜 : $it")
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                mainViewModel.localMarker.collect { markList ->
                    markList.forEach {
                        addMarker("", latitude = it.latitude.toDouble(), longitude = it.longitude.toDouble())
                    }
                }
            }
        }
    }

    private fun initMapView() {
        val mapViewContainer = binding.mapviewKakaomap
        mapView = MapView(this)
        mapViewContainer.addView(mapView)
    }

    private fun initListener() {
        binding.mainHistoryBtn.setOnClickListener {
            HistoryFragmentDialog().show(
                supportFragmentManager,
                "HistoryFragmentDialog"
            )
        }
        binding.mainSettingBtn.setOnClickListener {
            TimerFragmentDialog().show(
                supportFragmentManager,
                "SettingFragmentDialog"
            )
        }
    }

    // key hash값 얻기
//    private fun getAppKeyHash() {
//        try {
//            val info =
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                var md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val something = String(Base64.encode(md.digest(), 0))
//                Log.e("Hash key", something)
//            }
//        } catch (e: Exception) {
//
//            Log.e("name not found", e.toString())
//        }
//    }

    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어봄)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            // 권한이 있는 상태
            startTracking()
        }
    }

    // 권한 요청 후 행동
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인됨 (추적 시작)
                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
                startTracking()
            } else {
                // 권한 요청 후 거절됨 (다시 요청 or 토스트)
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 사용자 위치추적 시작
    @SuppressLint("MissingPermission")
    private fun startTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        //위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        // 현 위치에 마커 찍기
        val marker = MapPOIItem()
        marker.itemName = "현 위치"
        marker.mapPoint =uNowPosition
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)
    }

    // 위치추적 중지
    private fun stopTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }

    private fun addMarker(locationName: String, latitude: Double, longitude: Double) {
        val marker = MapPOIItem()

        marker.apply {
            itemName = locationName // 장소 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude) // 좌표
            markerType = MapPOIItem.MarkerType.BluePin // 기본 블루 마커
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커 클릭 시 기본 레드 핀
        }

        mapView.addPOIItem(marker)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
