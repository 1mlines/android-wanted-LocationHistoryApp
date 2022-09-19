package com.preonboarding.locationhistory.data.model

import java.io.Serializable
import java.util.*

data class Location(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val date: Date
): Serializable
