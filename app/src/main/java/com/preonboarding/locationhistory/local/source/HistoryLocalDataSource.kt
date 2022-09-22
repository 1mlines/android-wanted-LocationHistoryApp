package com.preonboarding.locationhistory.local.source

import androidx.lifecycle.LiveData
import com.preonboarding.locationhistory.local.HistoryDataSource
import com.preonboarding.locationhistory.local.dao.HistoryDao
import com.preonboarding.locationhistory.local.entity.History

class HistoryLocalDataSource internal constructor(
    private val historyDao: HistoryDao
) : HistoryDataSource {
    override fun insertHistory(latitude: Double, longitude: Double) {
        historyDao.insertHistory(latitude, longitude)
    }

    override fun findDistinctByDistance(): LiveData<List<History>> {
        return historyDao.findDistinctByDistance()
    }

    override fun findByDistanceAndCreatedAt(createdAt: String): LiveData<List<History>> {
        return historyDao.findByDistanceAndCreatedAt(createdAt)
    }
}