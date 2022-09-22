package com.preonboarding.locationhistory.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.data.HistoryDB
import com.preonboarding.locationhistory.data.HistoryRepository

class HistoryDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository

    init {
        val historyDao = HistoryDB.getDatabase(getApplication())!!.historyDao()
        repository = HistoryRepository(historyDao)
    }

    fun getHistory(date: String): LiveData<List<History>> {
        return repository.getHistory(date)
    }

    fun insertHistory(history: History) {
        repository.insert(history)
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoryDialogViewModel(application) as T
        }
    }

}
