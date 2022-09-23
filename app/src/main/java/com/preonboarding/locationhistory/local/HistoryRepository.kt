package com.preonboarding.locationhistory.local

import com.preonboarding.locationhistory.local.entity.History

interface HistoryRepository {
    suspend fun insertHistory(latitude: Double, longitude: Double)
    suspend fun findDistinctByDistance(): List<History>
    suspend fun findByDistanceAndCreatedAt(createdAt: String): List<History>
}