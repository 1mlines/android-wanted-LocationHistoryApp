package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.toEntity
import com.preonboarding.locationhistory.data.toModel
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAllLocations(): Flow<List<Location>> {
        return dataSource.getAllLocations().map { entities ->
            entities.map { entity ->
                entity.toModel()
            }
        }
    }
}