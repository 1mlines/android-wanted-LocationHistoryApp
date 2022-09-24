package com.preonboarding.locationhistory.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Created by 김현국 2022/09/20
 */

@Entity(
    tableName = "timer"
)
data class TimerEntity(

    @PrimaryKey
    val id: Int,

    val currentDuration: Long
)
