package com.preonboarding.locationhistory.presentation.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.preonboarding.locationhistory.R
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapViewContainer = findViewById<ViewGroup>(R.id.mapview_kakaomap)
        val mapView = MapView(this)
        mapViewContainer.addView(mapView)
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

    private fun createMarker(locationName: String, latitude: Double, longitude: Double): MapPOIItem {
        val marker = MapPOIItem()

        marker.apply {
            itemName = locationName // 장소 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude) // 좌표
            markerType = MapPOIItem.MarkerType.BluePin  // 기본 블루 마커
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커 클릭 시 기본 레드 핀
        }
        /*
        마커 띄우기
        mapView.addPOIItem(marker)
        */
        return marker
    }
}
