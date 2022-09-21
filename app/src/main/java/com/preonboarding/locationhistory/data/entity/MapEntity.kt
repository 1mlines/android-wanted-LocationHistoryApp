package com.preonboarding.locationhistory.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity
data class History(
    @PrimaryKey
    val date: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable {
    fun getDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        return dateFormat.format(date)
    }
}
