package com.preonboarding.locationhistory.data

import com.preonboarding.locationhistory.data.local.entity.LocationEntity
import com.preonboarding.locationhistory.domain.model.Location

fun Location.toEntity() = LocationEntity(
    id = 0,
    latitude = latitude,
    longitude = longitude,
    date = date
)

fun LocationEntity.toModel() = Location(
    id = id,
    latitude = latitude,
    longitude = longitude,
    date = date
)