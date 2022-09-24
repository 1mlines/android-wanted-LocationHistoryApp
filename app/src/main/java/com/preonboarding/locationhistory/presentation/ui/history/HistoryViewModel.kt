package com.preonboarding.locationhistory.presentation.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {

    private val currentDate = System.currentTimeMillis()

    private var _selectDate = MutableLiveData<Long>()
    val selectDate: LiveData<Long> get() = _selectDate

    init {
        updateSelectDate(currentDate)
    }

    fun updateSelectDate(selectDate: Long) {
        _selectDate.postValue(selectDate)
    }
}
