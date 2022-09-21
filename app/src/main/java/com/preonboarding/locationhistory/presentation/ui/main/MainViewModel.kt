package com.preonboarding.locationhistory.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

}