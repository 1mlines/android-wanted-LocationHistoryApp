package com.preonboarding.locationhistory.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>(mutableListOf())
    val locations: LiveData<List<Location>> = _locations

    private val _currentLocationSignal = MutableLiveData(false)
    val currentLocationSignal: LiveData<Boolean> = _currentLocationSignal


    fun showHistories(){
        viewModelScope.launch {
            _locations.value = repository.getLocations(-1)
        }
    }

    fun addLocation(location: Location) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }

    fun onLocationSignal() {
        _currentLocationSignal.value = true
    }

    fun offLocationSignal() {
        _currentLocationSignal.value = false
    }
}