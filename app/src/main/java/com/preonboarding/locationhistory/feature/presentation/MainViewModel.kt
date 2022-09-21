package com.preonboarding.locationhistory.feature.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.feature.history.domain.usecase.GetHistoryUseCase
import com.preonboarding.locationhistory.feature.history.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase
) : ViewModel() {

    private val _historyFromDate = MutableLiveData<List<History>?>()
    val historyFromDate: LiveData<List<History>?>
        get() = _historyFromDate

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
}