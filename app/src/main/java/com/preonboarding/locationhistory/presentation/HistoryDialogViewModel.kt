package com.preonboarding.locationhistory.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.data.HistoryDB
import com.preonboarding.locationhistory.data.HistoryRepository


class HistoryDialogViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun getHistory(date: String): LiveData<List<History>> {
        return repository.getHistory(date)
    }

    fun insertHistory(history: History) {
        repository.insert(history)
    }

}
