package com.preonboarding.locationhistory.data

import androidx.lifecycle.LiveData

class HistoryRepositoryImpl(private val historyDao: HistoryDao) : HistoryRepository {

    override fun insert(history: History) {
        historyDao.insertHistory(history)
    }

    override fun getHistory(date: String): LiveData<List<History>> {
        return historyDao.getHistory(date)
    }

}