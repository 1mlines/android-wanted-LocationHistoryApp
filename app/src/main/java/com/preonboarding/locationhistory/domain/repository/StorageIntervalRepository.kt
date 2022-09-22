package com.preonboarding.locationhistory.domain.repository

interface StorageIntervalRepository {

    fun getStorageInterval(): Long
    fun updateStorageInterval(interval: Long)
}
