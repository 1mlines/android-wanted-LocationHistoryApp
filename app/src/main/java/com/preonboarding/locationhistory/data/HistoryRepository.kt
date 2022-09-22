package com.preonboarding.locationhistory.data

import androidx.lifecycle.LiveData

interface HistoryRepository {

    fun insert(history: History)
    fun getHistory(date: String): LiveData<List<History>>

}