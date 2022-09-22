package com.preonboarding.locationhistory.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    val locations: Flow<List<Location>> = repository.getAllLocations()

    private val _currentLocationSignal = MutableLiveData(false)
    val currentLocationSignal: LiveData<Boolean> = _currentLocationSignal

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