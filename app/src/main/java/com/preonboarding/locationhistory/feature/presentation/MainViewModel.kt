package com.preonboarding.locationhistory.feature.presentation

import android.util.Log
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

    private val _historyFromDate = MutableLiveData<List<History>?>()
    val historyFromDate: LiveData<List<History>?>
        get() = _historyFromDate

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
            _historyFromDate.value = getHistoryUseCase(date)
        }
    }

    fun saveHistory(date: Long, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            runCatching {
                saveHistoryUseCase(date, latitude, longitude)
            }.onFailure {
                Log.e("saveHistory", "$it")
            }
        }
    }

    fun clearMarkerList() {
        viewModelScope.launch {
            _currentMarkerList.value = emptyList()
        }
    }

    fun addMarkerList(marker: MapPOIItem) {
        viewModelScope.launch {
            Log.d("marker", "marker: $marker")
            _currentMarkerList.update {
                val newList = mutableListOf<MapPOIItem>()
                newList.addAll(_currentMarkerList.value)
                newList.add(marker)
                newList.toList()
            }
            Log.d("marker", "markerList: ${_currentMarkerList.value.toString()}")
        }
    }

    private fun getSetTime() {
        viewModelScope.launch {
            _setTime.value = LocationHistoryApp.prefs.setTime
        }
    }

}