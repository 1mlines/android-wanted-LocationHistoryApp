package com.preonboarding.locationhistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Created by 김현국 2022/09/19
 */

@Entity(
    tableName = "locations"
)
data class LocationEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val latitude: Float,

    val longitude: Float,

    val date: String

)
