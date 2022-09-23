package com.preonboarding.locationhistory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey
    val num: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: String? = null,
    val latitude: Double,
    val longitude: Double
)