package com.preonboarding.locationhistory.data.source.local.datasource

import com.preonboarding.locationhistory.data.source.local.dao.TimerDao
import com.preonboarding.locationhistory.data.source.local.entity.TimerEntity
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/20
 */
class TimerDataSource @Inject constructor(
    private val timerDao: TimerDao
) {
    suspend fun getDuration(): Long? {
        return timerDao.getDuration(id = 0)
    }

    suspend fun setDuration(duration: Long): Long {
        return timerDao.insert(timerEntity = TimerEntity(id = 0, currentDuration = duration))
    }
}
