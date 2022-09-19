package com.preonboarding.locationhistory.data.source.local.datasource

import com.preonboarding.locationhistory.data.source.local.dao.LocationDao
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/19
 */
class LocationDataSource @Inject constructor(
    private val locationDao: LocationDao
) {
    suspend fun saveLocation(locationEntity: LocationEntity): Long {
        return locationDao.insert(location = locationEntity)
    }

    suspend fun getLocationsWithDate(date: String): List<LocationEntity> {
        return locationDao.getLocationsWithDate(date = date)
    }
}
