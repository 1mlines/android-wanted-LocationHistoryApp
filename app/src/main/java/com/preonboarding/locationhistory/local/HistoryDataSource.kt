package com.preonboarding.locationhistory.local

import androidx.lifecycle.LiveData
import com.preonboarding.locationhistory.local.entity.History

interface HistoryDataSource {
    fun insertHistory(latitude: Double, longitude: Double)
    fun findDistinctByDistance(): LiveData<List<History>>
    fun findByDistanceAndCreatedAt(createdAt: String): LiveData<List<History>>
}