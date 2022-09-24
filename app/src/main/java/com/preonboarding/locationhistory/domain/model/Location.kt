package com.preonboarding.locationhistory.domain.model

import java.sql.Timestamp


data class Location(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val date: Long
) {

    companion object {
        val EMPTY = Location(
            id = -1,
            latitude = 0f,
            longitude = 0f,
            date = Timestamp(System.currentTimeMillis()).time
        )
    }
}
