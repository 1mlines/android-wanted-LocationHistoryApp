package com.preonboarding.locationhistory.data.model

import java.io.Serializable

data class Location(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val date: Long
): Serializable
