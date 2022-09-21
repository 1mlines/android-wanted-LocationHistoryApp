package com.preonboarding.locationhistory.presentation.model

import java.util.Date

/**
 * @Created by 김현국 2022/09/19
 */
data class Location(

    val id: Int,

    val latitude: Float,

    val longitude: Float,

    val date: String
)
