package com.preonboarding.locationhistory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.Event
import com.preonboarding.locationhistory.local.HistoryRepository
import com.preonboarding.locationhistory.local.entity.History
import com.preonboarding.locationhistory.local.repository.HistoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HistoryRepositoryImpl,
) : ViewModel() {

    private val _dateName: MutableLiveData<String> = MutableLiveData()
    val dateName: MutableLiveData<String>
        get() = _dateName

    private val _historyResponse: MutableLiveData<List<History>> = MutableLiveData()
    val historyResponse: MutableLiveData<List<History>>
        get() = _historyResponse

    private val _historyList: MutableLiveData<List<History>> = MutableLiveData()
    val historyList: MutableLiveData<List<History>>
        get() = _historyList

    private val _dialogConfirm: MutableLiveData<Event<Unit>> = MutableLiveData()
    val dialogConfirm: MutableLiveData<Event<Unit>>
        get() = _dialogConfirm

    private val _dialogCancel: MutableLiveData<Event<Unit>> = MutableLiveData()
    val dialogCancel: MutableLiveData<Event<Unit>>
        get() = _dialogCancel

    private val _dialogDatePicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    val dialogDatePicker: MutableLiveData<Event<Unit>>
        get() = _dialogDatePicker

    fun changeDateName(name: String) {
        _dateName.value = name
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
            _historyResponse.value = repository.findByDistanceAndCreatedAt(createdAt)
        }
    }

    fun dialogDatePicker() {
        _dialogDatePicker.value = Event(Unit)
    }

    fun dialogConfirm() {
        _historyList.value = _historyResponse.value
        _dialogConfirm.value = Event(Unit)
    }

    fun dialogCancel() {
        _dialogCancel.value = Event(Unit)
    }
}