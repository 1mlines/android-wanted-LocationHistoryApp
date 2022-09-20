package com.preonboarding.locationhistory.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.locationhistory.data.model.Location
import com.preonboarding.locationhistory.data.repository.LocationRepositoryImpl
import com.preonboarding.locationhistory.di.LocalDataSource
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: LocationRepository): ViewModel() {
    fun insert(location: Location) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }

    fun getLocation(date: Long){
        viewModelScope.launch {
            val response = repository.getLocations(date)
            response.forEach {
                Log.d("response", "${it.date}, ${it.latitude}, ${it.longitude}, ${it.id}")
            }
        }
    }
}