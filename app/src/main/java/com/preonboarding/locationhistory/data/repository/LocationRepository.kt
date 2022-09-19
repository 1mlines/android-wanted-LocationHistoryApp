package com.preonboarding.locationhistory.data.repository

import androidx.lifecycle.LiveData
import com.preonboarding.locationhistory.data.local.dao.LocationDao
import com.preonboarding.locationhistory.data.local.entity.LocationEntity

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun getLocations(date: Long): List<LocationEntity> {
        return locationDao.getLocations(date = date)
    }

    suspend fun insertLocation(locationEntity: LocationEntity) {
        locationDao.insert(location = locationEntity)
    }
}