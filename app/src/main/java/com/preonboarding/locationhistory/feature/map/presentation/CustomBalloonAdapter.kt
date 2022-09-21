package com.preonboarding.locationhistory.feature.map.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.preonboarding.locationhistory.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class CustomBalloonAdapter(inflater: LayoutInflater) : CalloutBalloonAdapter {

    val callOutBalloon: View = inflater.inflate(R.layout.item_balloon, null)
    val name = callOutBalloon.findViewById<TextView>(R.id.tv_name)
    val address = callOutBalloon.findViewById<TextView>(R.id.tv_address)

    // 마커 클릭 시 나오는 말풍선
    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        val marker = MapPOIItem()
        name.text = marker?.itemName
        address.text = "getCalloutBalloon"
        return callOutBalloon
    }

    // 말풍선 클릭 시
    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        address.text = "getPressedCalloutBalloon"
        return callOutBalloon
    }
}