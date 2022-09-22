package com.preonboarding.locationhistory.domain.repository

import com.preonboarding.locationhistory.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocations(date: Long): List<Location>
    suspend fun insertLocation(location: Location)
    fun getAllLocations(): Flow<List<Location>>
}