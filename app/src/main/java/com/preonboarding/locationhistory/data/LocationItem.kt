package com.preonboarding.locationhistory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class LocationItem(
    var num:Int,
    @PrimaryKey
    val datetime:Date,
    val altitude:Double,
    val latitude:Double
)