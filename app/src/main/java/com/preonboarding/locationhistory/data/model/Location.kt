package com.preonboarding.locationhistory.data.model

import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import com.preonboarding.locationhistory.presentation.model.Location as LocationModel

/**
 * @Created by 김현국 2022/09/19
 */

fun LocationEntity.asModel() = LocationModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    date = date
)
