package com.preonboarding.locationhistory.data.di

import androidx.room.withTransaction
import com.preonboarding.locationhistory.data.local.database.LocationDatabase
import com.preonboarding.locationhistory.data.local.entity.LocationEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val locationDatabase: LocationDatabase){
    private val locationDao = locationDatabase.locationDao()

    suspend fun getLocations(date: Long): List<LocationEntity> {
        return locationDao.getLocations(date = date)
    }

    suspend fun insertLocation(locationEntity: LocationEntity) {
        locationDatabase.withTransaction {
            locationDao.insertLocation(location = locationEntity)
        }
    }
}