package com.preonboarding.locationhistory.presentation.uistates

/**
 * @Created by 김현국 2022/09/21
 */
sealed class DurationUiStates {
    data class Success(val duration: Long) : DurationUiStates()
    object DurationSaveSuccess : DurationUiStates()
    object Loading : DurationUiStates()
}
