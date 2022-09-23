package com.preonboarding.locationhistory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.Event
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

    private val _historyList: MutableLiveData<List<History>> = MutableLiveData(emptyList())
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

    private val _currentAddress = MutableLiveData<String>()
    val currentAddress: LiveData<String>
        get() = _currentAddress

    private val _saveInterval = MutableLiveData<Long>(60000L)
    val saveInterval: LiveData<Long>
        get() = _saveInterval

    private val _showAddressDialog = MutableLiveData<Event<Unit>>()
    val showAddressDialog: LiveData<Event<Unit>>
        get() = _showAddressDialog

    private val _showSettingDialog = MutableLiveData<Event<Unit>>()
    val showSettingDialog: LiveData<Event<Unit>>
        get() = _showSettingDialog

    private val _defaultHistoryData = MutableLiveData<List<History>>(emptyList())
    val defaultHistoryData: LiveData<List<History>>
        get() = _defaultHistoryData // 기본

    fun changeDateName(name: String) {
        _dateName.value = name
        findByDistanceAndCreatedAt(name)
    }

    fun changeSaveInterval(minute: Long) {
        _saveInterval.value = minute
    }

    fun insertHistory(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.insertHistory(latitude, longitude)
        }
    }

    fun findDistinctByDistance() {
        viewModelScope.launch {
            val historyList = repository.findDistinctByDistance()
            _defaultHistoryData.value = historyList
        }
    }

    fun findByDistanceAndCreatedAt(createdAt: String) {
        viewModelScope.launch {
            _historyList.value = repository.findByDistanceAndCreatedAt(createdAt)
        }
    }

    fun showAddressDialog() {
        _showAddressDialog.value = Event(Unit)
    }

    fun showSettingDialog() {
        _showSettingDialog.value = Event(Unit)
    }

    fun dialogDatePicker() {
        _dialogDatePicker.value = Event(Unit)
    }

    fun dialogConfirm() {
        if (_dateName.value != null) findByDistanceAndCreatedAt(_dateName.value ?: "")
        _dialogConfirm.value = Event(Unit)
    }

    fun dialogCancel() {
        _dialogCancel.value = Event(Unit)
    }

    fun showCurrentAddress(address: String) {
        _currentAddress.value = address
    }
}