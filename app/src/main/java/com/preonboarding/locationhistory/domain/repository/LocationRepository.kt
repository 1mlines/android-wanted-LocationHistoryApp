package com.preonboarding.locationhistory.domain.repository

import com.preonboarding.locationhistory.domain.model.Location

interface LocationRepository {

    suspend fun getLocations(date: Long): List<Location>
    suspend fun insertLocation(location: Location)
}