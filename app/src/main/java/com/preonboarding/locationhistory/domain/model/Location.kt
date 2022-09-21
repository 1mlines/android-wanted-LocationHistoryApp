package com.preonboarding.locationhistory.domain.model

data class Location(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val date: Long
)
