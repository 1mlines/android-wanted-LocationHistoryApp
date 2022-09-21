package com.preonboarding.locationhistory.view

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.preonboarding.locationhistory.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {

    val calloutBalloon: View = inflater.inflate(R.layout.item_balloon, null)
    val name = calloutBalloon.findViewById<TextView>(R.id.tv_name)
    val address = calloutBalloon.findViewById<TextView>(R.id.tv_address)

    // 마커 클릭 시 나오는 말풍선
    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        val marker = MapPOIItem()
        name.text = marker?.itemName
        address.text = "getCalloutBalloon"
        return calloutBalloon
    }

    // 말풍선 클릭 시
    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        address.text = "getPressedCalloutBalloon"
        return calloutBalloon
    }
}