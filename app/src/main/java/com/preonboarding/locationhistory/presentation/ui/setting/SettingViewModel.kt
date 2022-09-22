package com.preonboarding.locationhistory.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.preonboarding.locationhistory.domain.repository.StorageIntervalRepository
import com.preonboarding.locationhistory.presentation.ui.worker.CurrentLocationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: StorageIntervalRepository,
) : ViewModel() {

    private var _storageInterval: MutableLiveData<Long> = MutableLiveData()
    val storageInterval: LiveData<Long> get() = _storageInterval

    fun getStorageInterval() {
        _storageInterval.postValue(repository.getStorageInterval())
    }

    fun updateStorageInterval(interval: Long) {
        repository.updateStorageInterval(interval)
    }
}
