package com.preonboarding.locationhistory.data.model

import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import java.text.SimpleDateFormat
import java.util.Date
import com.preonboarding.locationhistory.presentation.model.Location as LocationModel

/**
 * @Created by 김현국 2022/09/19
 */
// fun Location.asModel() = LocationModel(
//    latitude = latitude,
//    longitude =  longitude,
//    date =
// )

val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
fun LocationModel.asEntity() = LocationEntity(
    id = 0,
    latitude = latitude,
    longitude = longitude,
    date = simpleDateFormat.format(date)
)

fun LocationEntity.asModel() = LocationModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    date = simpleDateFormat.parse(date) ?: Date()
)

fun formatDateToString(date: Date): String {
    return simpleDateFormat.format(date)
}
