package com.preonboarding.locationhistory.data.repository

import com.preonboarding.locationhistory.data.source.local.datasource.TimerDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/20
 */
class TimerRepository @Inject constructor(
    private val timerDataSource: TimerDataSource
) {
    fun getDuration(): Flow<Long> {
        return flow {
            val result = timerDataSource.getDuration()
            if (result != null) {
                emit(result)
            } else {
                emit(15)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setDuration(duration: Long): Flow<Long> {
        return flow {
            emit(timerDataSource.setDuration(duration = duration))
        }.flowOn(Dispatchers.IO)
    }
}
