package com.preonboarding.locationhistory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.Event
import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.entity.History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HistoryRepository,
) : ViewModel() {

    val dateName: MutableLiveData<String> = MutableLiveData()
    val historyResponse: MutableLiveData<List<History>> = MutableLiveData()
    val historyList: MutableLiveData<List<History>> = MutableLiveData()
    var dialogConfirm: MutableLiveData<Event<Unit>> = MutableLiveData()
    var dialogCancel: MutableLiveData<Event<Unit>> = MutableLiveData()
    var dialogDatePicker: MutableLiveData<Event<Unit>> = MutableLiveData()

    fun changeDateName(name: String) {
        dateName.value = name
    }

    fun insertHistory(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.insertHistory(latitude, longitude)
        }
    }

    fun findDistinctByDistance() {
        viewModelScope.launch {
            repository.findDistinctByDistance()
        }
    }

    fun findByDistanceAndCreatedAt(createdAt: String) {
        viewModelScope.launch {
            historyResponse.value = repository.findByDistanceAndCreatedAt(createdAt)
        }
    }

    fun dialogDatePicker() {
        dialogDatePicker.value = Event(Unit)
    }

    fun dialogConfirm() {
        historyList.value = historyResponse.value
        dialogConfirm.value = Event(Unit)
    }

    fun dialogCancel() {
        dialogCancel.value = Event(Unit)
    }
}