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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.preonboarding.locationhistory.LocationHistoryApp
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.base.BaseActivity
import com.preonboarding.locationhistory.data.entity.toFormatDate
import com.preonboarding.locationhistory.data.entity.toMapItem
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.feature.history.presentation.HistoryDialog
import com.preonboarding.locationhistory.feature.map.presentation.CustomBalloonAdapter
import com.preonboarding.locationhistory.feature.set.SetTimeDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val ACCESS_FINE_LOCATION = 1000
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            startTracking()
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    private lateinit var balloonAdapter: CustomBalloonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        permissionCheck()
        initView()
        observeToObservable()
        markerInit()
    }

    private fun initView() {
        balloonAdapter = CustomBalloonAdapter(layoutInflater, mainViewModel, this)
        binding.apply {
            btnHistory.setOnClickListener {
                val historyDialog = HistoryDialog(mainViewModel)
                historyDialog.show(this@MainActivity.supportFragmentManager, "hd")
            }
            btnSetting.setOnClickListener {
                val dialog = SetTimeDialog()
                dialog.show(supportFragmentManager, "다이얼로그")
            }
            btnAddress.setOnClickListener {
                if (checkLocationService()) {
                    startTracking()
                    val builder = AlertDialog.Builder(this@MainActivity)
                    val location = getCurrentLocation()
                    val detailAddress = getDetailAddress(location.first, location.second)
                    builder.apply {
                        setTitle("현재 위치")
                        setMessage(detailAddress)
                    }.show()
                } else {
                    Toast.makeText(this@MainActivity, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
                }
            }
            mapView.setCalloutBalloonAdapter(balloonAdapter)
        }
    }

    private fun permissionCheck() {
        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

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
                if (LocationHistoryApp.prefs.isFirst) {
                    // 최초 권한 요청
                    LocationHistoryApp.prefs.isFirst = false
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

    @SuppressLint("MissingPermission")
    private fun observeToObservable() {
        mainViewModel.apply {
            setTime.observe(this@MainActivity) { time ->
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    time.toString().toLong(),
                    0F,
                    gpsLocationListener
                )//콜백으로 설정
            }
        }
        lifecycleScope.launch {
            mainViewModel.historyFromDate.collect { historyList ->
                updateMarkerList(historyList.toMapItem())
            }
        }
    }

    private fun updateMarkerList(
        markerList: List<MapPOIItem>
    ) {
        binding.mapView.removeAllPOIItems()
        binding.mapView.addPOIItems(markerList.toTypedArray())
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

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        val userNowLocation: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        //위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        // 현 위치에 마커 찍기
        val marker = MapPOIItem().apply {
            itemName = "현 위치"
            mapPoint = uNowPosition
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        mainViewModel.saveHistory(getCurrentTime(), uLatitude, uLongitude)
        updateHistory()
        Toast.makeText(this, "lat: $uLatitude, long: $uLongitude", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Pair<Double, Double> {
        val userNowLocation: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        //위도 , 경도
        val uLatitude = userNowLocation!!.latitude
        val uLongitude = userNowLocation!!.longitude
        return Pair(uLatitude, uLongitude)
    }

    private fun getDetailAddress(uLatitude: Double, uLongitude: Double): String {
        val geocoder = Geocoder(this)
        val convertAddress =
            geocoder.getFromLocation(uLatitude, uLongitude, 1).get(0).getAddressLine(0)
        return convertAddress.toString()
    }

    private fun markerInit() {
        binding.apply {
            btnReload.setOnClickListener {
                mapView.removeAllPOIItems()
            }
            mainViewModel.clearMarkerList()
        }
    }

    private fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    private fun updateHistory() {
        mainViewModel.getHistoryFromDate(getCurrentTime().toFormatDate())
    }
}
