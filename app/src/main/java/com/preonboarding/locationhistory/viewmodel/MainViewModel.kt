package com.preonboarding.locationhistory.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){

    val dateName: MutableLiveData<String> = MutableLiveData()

    fun changeDateName(name: String) {
        dateName.value = name
    }

}