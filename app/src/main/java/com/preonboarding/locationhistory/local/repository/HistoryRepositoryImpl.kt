package com.preonboarding.locationhistory.local.repository

import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.dao.HistoryDao
import com.preonboarding.locationhistory.local.entity.History

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override fun insertHistory(latitude: Double, longitude: Double) {
        historyDao.insertHistory(latitude, longitude)
    }

    override fun findDistinctByDistance(): List<History> {
        return historyDao.findAll()
    }

    override fun findByDistanceAndCreatedAt(createdAt: String): List<History> {
        return historyDao.findByCreatedAt(createdAt)
    }
}