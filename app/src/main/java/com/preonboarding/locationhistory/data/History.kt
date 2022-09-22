package com.preonboarding.locationhistory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class History(
    @PrimaryKey
    var num: Int,
  //  val dateTime: Date,
    val latitude: Double,
    val longitude: Double
)