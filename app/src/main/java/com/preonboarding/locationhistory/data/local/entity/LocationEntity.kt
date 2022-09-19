package com.preonboarding.locationhistory.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float
)