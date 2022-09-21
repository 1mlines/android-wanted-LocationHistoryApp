package com.preonboarding.locationhistory.presentation

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import androidx.work.*
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

const val WORK_REPEAT_INTERVAL: Long = 15


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment

    @RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    private val constraint: Constraints by lazy {
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
    private val workRequest =
        PeriodicWorkRequestBuilder<SaveHistoryWorker>(WORK_REPEAT_INTERVAL, TimeUnit.MINUTES)// 15분 반복
            .setConstraints(constraint) // 작업을 재시도 할경우에 대한 정책
            .setBackoffCriteria( // 최소 시간으로 Retry
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserPermission()

        initMap()
        bindViews()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkUserPermission(){
        val permissions:Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

//        var denied = permissions.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }
//        if (denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permissions.values.toTypedArray(), Constants.REQUEST_PERMISSIONS)
//        }

        if ( checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions ,10 )
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "위치 사용를 사용 할 수 있습니다", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ( requestCode == PackageManager.PERMISSION_GRANTED ){
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, " 위치 권한이 필요합니다. ", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

    }
}