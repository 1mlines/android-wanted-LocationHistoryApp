package com.preonboarding.locationhistory.presentation

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.common.Constants.WORK_REPEAT_INTERVAL
import com.preonboarding.locationhistory.common.Constants.WORK_SAVE_HISTORY
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String = MainActivity::class.java.name
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserPermission()

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


    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkUserPermission() {
        val permissions: Array<String> =
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

//        var denied = permissions.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }
//        if (denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permissions.values.toTypedArray(), Constants.REQUEST_PERMISSIONS)
//        }

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 10)
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
        if (requestCode == PackageManager.PERMISSION_GRANTED) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, " 위치 권한이 필요합니다. ", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initMap() {
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


    override fun onDestroy() {
        workManager.cancelUniqueWork(WORK_SAVE_HISTORY)
        super.onDestroy()
    }
}
