package com.preonboarding.locationhistory.feature.history.domain.usecase

import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.data.entity.toFormatDate
import com.preonboarding.locationhistory.domain.MapRepository
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