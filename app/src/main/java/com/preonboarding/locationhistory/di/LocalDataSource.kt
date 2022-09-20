package com.preonboarding.locationhistory.di

import com.preonboarding.locationhistory.data.local.dao.LocationDao
import com.preonboarding.locationhistory.data.local.entity.LocationEntity

class LocalDataSource(private val locationDao: LocationDao) {
    suspend fun getLocations(date: Long): List<LocationEntity> {
        return locationDao.getLocations(date = date)
    }

    suspend fun insertLocation(locationEntity: LocationEntity) {
        locationDao.insertLocation(location = locationEntity)
    }
}