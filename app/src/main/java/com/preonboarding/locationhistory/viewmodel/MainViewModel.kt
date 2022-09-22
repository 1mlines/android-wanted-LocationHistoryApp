package com.preonboarding.locationhistory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.preonboarding.locationhistory.Event
import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.entity.History
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HistoryRepository,
) : ViewModel() {

    val dateName: MutableLiveData<String> = MutableLiveData()
    val historyResponse: MutableLiveData<List<History>> = MutableLiveData()
    val historyList: MutableLiveData<List<History>> = MutableLiveData()
    var dialog: MutableLiveData<Event<Unit>> = MutableLiveData()

    fun changeDateName(name: String) {
        dateName.value = name
    }

    suspend fun insertHistory(latitude: Double, longitude: Double) {
        repository.insertHistory(latitude, longitude)
    }

    suspend fun findDistinctByDistance(): List<History> {
        return repository.findDistinctByDistance()
    }

    suspend fun findByDistanceAndCreatedAt(createdAt: String): List<History> {
        return repository.findByDistanceAndCreatedAt(createdAt)
    }

    fun dialogConfirm() {
        historyList.value = historyResponse.value
        dialog.value = Event(Unit)
    }
}