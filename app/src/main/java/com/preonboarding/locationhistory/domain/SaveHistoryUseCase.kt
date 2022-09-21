package com.preonboarding.locationhistory.domain

import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.data.entity.toFormatDate
import com.preonboarding.locationhistory.data.entity.toFormatTime
import javax.inject.Inject

class SaveHistoryUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(date: Long, latitude: Double, longitude: Double) {
        val history = History(date.toFormatTime(), date.toFormatDate(), latitude, longitude)
        runCatching {
            mapRepository.saveHistory(history)
        }.onFailure { e ->
            throw e
        }
    }
}