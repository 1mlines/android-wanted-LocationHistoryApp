package com.preonboarding.locationhistory.presentation.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private var calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _currentDate: MutableStateFlow<String> =
        MutableStateFlow("")
    val currentDate: StateFlow<String>
        get() = _currentDate

    // 오늘 날짜 받아오기
    fun initCurrentDate() {
        val datePattern = "yyyy.MM.dd"
        _currentDate.value =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(Date(System.currentTimeMillis()))

        Timber.tag(TAG).e("오늘 날짜 : ${_currentDate.value}")
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

    companion object {
        private const val TAG = "MainViewModel"
    }
}