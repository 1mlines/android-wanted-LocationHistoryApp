package com.preonboarding.locationhistory.feature.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.base.BaseActivity
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.feature.history.presentation.HistoryDialog
import com.preonboarding.locationhistory.feature.map.presentation.CustomBalloonAdapter
import com.preonboarding.locationhistory.feature.set.SetTimeDialog
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val ACCESS_FINE_LOCATION = 1000

    private val mainViewModel: MainViewModel by viewModels()

    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude

            Log.e("gpsLocationListener", "$latitude $altitude")
        }

        //아래 3개함수는 형식상 필수부분
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickBtnAddress()

        initView()
        markerInit()
        // 커스텀 말풍선 등록
        binding.mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

    }

    private fun initView() {
        binding.apply {
            btnHistory.setOnClickListener {
                val dialog = HistoryDialog(mainViewModel)
                dialog.show(this@MainActivity.supportFragmentManager, "hd")
            }
            btnSetting.setOnClickListener {
                val dialog = SetTimeDialog()
                dialog.show(supportFragmentManager, "다이얼로그")
            }
        }
    }


    private fun clickBtnAddress() {
        binding.btnAddress.setOnClickListener {
            if (checkLocationService()) {
                permissionCheck()
            } else {
                Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                denyAccess("현재 위치를 확인하시려면 위치 권한을 허용해주세요.", "취소")
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        ACCESS_FINE_LOCATION
                    )
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    denyAccess("현재 위치를 확인하시려면 위치 권한을 허용해주세요.", "설정으로 이동")
                }
            }
        } else {
            // 권한이 있는 상태
            startTracking()
        }
    }

    private fun denyAccess(setMessage: String, positiveBtn: String) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage(setMessage)
            setPositiveButton(positiveBtn) { dialog, which ->
                ActivityCompat.requestPermissions(
                    Activity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_FINE_LOCATION
                )
            }
            setNegativeButton("취소") { dialog, which ->

            }
            show()
        }
    }

    // 권한 요청 후 행동
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    // 위치추적 시작
    private fun startTracking1() {
        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        mainViewModel.setTime.observe(this) {
            lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10 * 60 * it.toString().toLong(),
                0F,
                gpsLocationListener
            )//콜백으로 설정
        }

        //위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        // 현 위치에 마커 찍기
        val marker = MapPOIItem()
        marker.itemName = "현 위치"
        marker.mapPoint = uNowPosition
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        binding.mapView.addPOIItem(marker)

        Toast.makeText(this, "lat: $uLatitude, long: $uLongitude", Toast.LENGTH_SHORT).show()
        Log.d("locataion", "startTracking: lat: $uLatitude, long: $uLongitude")

        // 위도, 경도로 상세 주소 받아오기
        val geocoder = Geocoder(this)
        val convertAddress = geocoder.getFromLocation(uLatitude, uLongitude, 1)
        binding.tvAddress.text = convertAddress.toString()
    }

    private fun markerInit() {
        binding.btnReload.setOnClickListener {
            binding.mapView.removeAllPOIItems() // 마커 제거 가능!!!!! (초기화)
        }
    }
}