package com.preonboarding.locationhistory.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.data.repository.LocationRepository
import com.preonboarding.locationhistory.presentation.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val locationRepository: LocationRepository
): ViewModel() {

    var calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _currentDate: MutableStateFlow<String> =
        MutableStateFlow("")
    val currentDate: StateFlow<String>
        get() = _currentDate

    private val _currentHistory: MutableStateFlow<MutableList<Location>> =
        MutableStateFlow(mutableListOf())
    val currentHistory: StateFlow<MutableList<Location>>
        get() = _currentHistory

    // 오늘 날짜 받아오기
    fun initCurrentDate() {
        val datePattern = "yyyy.MM.dd"
        _currentDate.value =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(Date(System.currentTimeMillis()))
    }

    fun updateCurrentDate(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy.MM.dd"
        _currentDate.value =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)

        Timber.tag(TAG).e("선택한 날짜 : ${_currentDate.value}")
    }

    // history
    fun getHistoryWithDate() {
        viewModelScope.launch {
            kotlin.runCatching {
                locationRepository.getLocationsWithDate(date = _currentDate.value)
                    .collect {
                        _currentHistory.value = it.toMutableList()
                    }
            }
                .onSuccess {
                    Timber.tag(TAG).e("success get history")
                }
                .onFailure {
                    Timber.tag(TAG).e(it)
                }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}