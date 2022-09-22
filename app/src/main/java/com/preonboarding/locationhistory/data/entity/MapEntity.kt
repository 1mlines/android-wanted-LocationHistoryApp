package com.preonboarding.locationhistory.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import java.text.SimpleDateFormat

@Parcelize
@Entity
data class History(
    val time: String,

    val date: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun Long.toFormatTime(): String = SimpleDateFormat("yyyy.MM.dd HH:mm").format(this)

fun Long.toFormatDate(): String = SimpleDateFormat("yyyy.MM.dd").format(this)

fun List<History>.toMapItem(): List<MapPOIItem> {
    return this.map {
        val position = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
        MapPOIItem().apply {
            itemName = "날짜별 위치"
            mapPoint = position
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
    }
}