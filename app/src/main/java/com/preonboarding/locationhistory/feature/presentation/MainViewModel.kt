package com.preonboarding.locationhistory.feature.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.LocationHistoryApp
import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.feature.history.domain.usecase.GetHistoryUseCase
import com.preonboarding.locationhistory.feature.history.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase
) : ViewModel() {

    private val _historyFromDate = MutableStateFlow<List<History>>(emptyList())
    val historyFromDate = _historyFromDate.asStateFlow()

    private val _setTime = MutableLiveData<String>()
    val setTime: LiveData<String>
        get() = _setTime

    private val _currentMarkerList = MutableStateFlow<List<MapPOIItem>>(emptyList())
    val currentMarkerList = _currentMarkerList.asStateFlow()

    init {
        getSetTime()
    }

    fun getHistoryFromDate(date: String) {
        viewModelScope.launch {
            _historyFromDate.update {
                getHistoryUseCase(date)
            }
        }
    }

    fun saveHistory(date: Long, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            saveHistoryUseCase(date, latitude, longitude)
        }
    }

    fun clearMarkerList() {
        viewModelScope.launch {
            _currentMarkerList.value = emptyList()
        }
    }

    private fun getSetTime() {
        viewModelScope.launch {
            _setTime.value = LocationHistoryApp.prefs.setTime
        }
    }

}