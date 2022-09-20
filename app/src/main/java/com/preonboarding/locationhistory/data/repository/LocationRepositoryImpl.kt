package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.model.Location
import com.preonboarding.locationhistory.data.toEntity
import com.preonboarding.locationhistory.data.toModel
import com.preonboarding.locationhistory.di.LocalDataSource
import com.preonboarding.locationhistory.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val dataSource: LocalDataSource
) : LocationRepository {
    override suspend fun getLocations(date: Long): List<Location> {
        return dataSource.getLocations(date)
            .map { it.toModel() }
    }

    override suspend fun insertLocation(location: Location) {
        dataSource.insertLocation(location.toEntity())
    }
}