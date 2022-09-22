package com.preonboarding.locationhistory.local

import com.preonboarding.locationhistory.local.entity.History

interface HistoryRepository {
    fun insertHistory(latitude: Double, longitude: Double)
    fun findDistinctByDistance(): List<History>
    fun findByDistanceAndCreatedAt(createdAt: String): List<History>
}