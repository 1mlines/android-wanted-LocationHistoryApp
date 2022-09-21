package com.preonboarding.locationhistory.presentation.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.presentation.custom.dialog.bottom.HistoryBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.initCurrentDate()

        bindingViewModel()
        initMapView()
        initListener()

    }

    private fun bindingViewModel() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.currentDate.collect {
                Timber.tag(TAG).e("오늘 날짜 : $it")
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
            HistoryBottomSheetFragment().show(
                supportFragmentManager, "HistoryBottomSheetFragment"
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

    private fun addMarker(locationName: String, latitude: Double, longitude: Double) {
        val marker = MapPOIItem()

        marker.apply {
            itemName = locationName // 장소 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude) // 좌표
            markerType = MapPOIItem.MarkerType.BluePin  // 기본 블루 마커
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커 클릭 시 기본 레드 핀
        }

        mapView.addPOIItem(marker)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
