package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.database.MapDao
import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.domain.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapDao: MapDao
) : MapRepository {

    override suspend fun getHistoryFromDate(date: String): List<History>? =
        mapDao.getHistoryFromDate(date)

    override suspend fun saveHistory(history: History) = mapDao.saveHistory(history)
}