package com.preonboarding.locationhistory.domain

import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.data.entity.toFormatDate
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(date: Long): List<History>? {
        val formatDate = date.toFormatDate()
        return runCatching {
            mapRepository.getHistoryFromDate(formatDate)
        }.getOrNull()
    }
}