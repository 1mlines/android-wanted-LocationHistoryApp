package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.model.asModel
import com.preonboarding.locationhistory.data.source.local.datasource.LocationDataSource
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import com.preonboarding.locationhistory.presentation.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/19
 */
class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource
) {
    suspend fun saveLocation(location: LocationEntity): Long {
        return locationDataSource.saveLocation(location)
    }

    fun getLocationsWithDate(date: String): Flow<List<Location>> {
        return flow {
            val locations = locationDataSource.getLocationsWithDate(date = date)
            if (locations.isNotEmpty()) {
                emit(
                    locations.map { entity ->
                        entity.asModel()
                    }
                )
            } else {
                emit(emptyList())
            }
        }
    }

    fun getLocations(): Flow<List<LocationEntity>> {
        return locationDataSource.getLocations()
    }
}
