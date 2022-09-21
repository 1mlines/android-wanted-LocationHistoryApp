package com.preonboarding.locationhistory.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

@Parcelize
@Entity
data class History(
    @PrimaryKey
    val time: String,

    val date: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable

fun Long.toFormatTime(date: Long): String = SimpleDateFormat("yyyy.MM.dd HH:mm").format(date)

fun Long.toFormatDate(date: Long): String = SimpleDateFormat("yyyy.MM.dd").format(date)

