package com.preonboarding.locationhistory.local.repository

import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.dao.HistoryDao
import com.preonboarding.locationhistory.local.entity.History
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override suspend fun insertHistory(latitude: Double, longitude: Double) {
        historyDao.insertHistory(latitude, longitude)
    }

    override suspend fun findDistinctByDistance(): List<History> {
        return historyDao.findAll()
    }

    override suspend fun findByDistanceAndCreatedAt(createdAt: String): List<History> {
        return historyDao.findByCreatedAt(createdAt)
    }
}