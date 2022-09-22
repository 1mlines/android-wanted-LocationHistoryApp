package com.preonboarding.locationhistory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.data.HistoryDao
import kotlinx.coroutines.flow.Flow
import java.util.*

class HistoryDialogViewModel(private val historyDao: HistoryDao) : ViewModel() {

   // fun getHistory(date: Date): Flow<List<History>> = historyDao.getHistory(myDate = date)

    fun insertHistory(history: History) = historyDao.insertHistory(myHistory = history)

}

