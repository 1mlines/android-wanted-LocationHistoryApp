package com.preonboarding.locationhistory.data.repository

import androidx.room.withTransaction
import com.preonboarding.locationhistory.data.db.database.LocationDatabase
import com.preonboarding.locationhistory.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow
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

    fun getAllLocations(): Flow<List<LocationEntity>> {
        return locationDao.getAllLocations()
    }
}