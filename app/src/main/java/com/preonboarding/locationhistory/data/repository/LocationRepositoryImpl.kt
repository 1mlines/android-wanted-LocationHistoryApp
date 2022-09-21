package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.toEntity
import com.preonboarding.locationhistory.data.toModel
import com.preonboarding.locationhistory.data.di.LocalDataSource
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource
) : LocationRepository {

    override suspend fun getLocations(date: Long): List<Location> {
        return dataSource.getLocations(date)
            .map { it.toModel() }
    }

    override suspend fun insertLocation(location: Location) {
        dataSource.insertLocation(location.toEntity())
    }

    override suspend fun getAllLocations(): List<Location> {
        return dataSource.getAllLocations()
            .map { it.toModel() }
    }
}