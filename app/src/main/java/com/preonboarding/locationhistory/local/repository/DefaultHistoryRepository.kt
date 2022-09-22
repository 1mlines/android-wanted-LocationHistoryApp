package com.preonboarding.locationhistory.local.repository

import androidx.lifecycle.LiveData
import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.entity.History
import com.preonboarding.locationhistory.local.source.HistoryLocalDataSource

class DefaultHistoryRepository(
    private val historyLocalDataSource: HistoryLocalDataSource
) : HistoryRepository {
    override fun insertHistory(latitude: Double, longitude: Double) {
        historyLocalDataSource.insertHistory(latitude, longitude)
    }

    override fun findDistinctByDistance(): LiveData<List<History>> {
        return historyLocalDataSource.findDistinctByDistance()
    }

    override fun findByDistanceAndCreatedAt(createdAt: String): LiveData<List<History>> {
        return historyLocalDataSource.findByDistanceAndCreatedAt(createdAt)
    }
}