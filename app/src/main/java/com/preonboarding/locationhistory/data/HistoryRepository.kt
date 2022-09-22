package com.preonboarding.locationhistory.data

import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDao: HistoryDao) {

    fun insert(history: History) {
        historyDao.insertHistory(history)
    }

    fun getHistory(date: String): LiveData<List<History>> {
        return historyDao.getHistory(date)
    }

}