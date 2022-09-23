package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.domain.repository.StorageIntervalRepository
import javax.inject.Inject

class StorageIntervalRepositoryImpl @Inject constructor(
    private val dataSource: StorageIntervalDataSource
) : StorageIntervalRepository {

    override fun getStorageInterval(): Long {
        return dataSource.getStorageInterval()
    }

    override fun updateStorageInterval(interval: Long) {
        dataSource.updateStorageInterval(interval)
    }
}
