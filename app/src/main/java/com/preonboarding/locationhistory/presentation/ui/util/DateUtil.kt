package com.preonboarding.locationhistory.presentation.ui.util

import java.text.SimpleDateFormat
import java.util.Locale

val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
val currentDate: String = System.currentTimeMillis().convertTimeStampToDate()
val currentDateData: Long = convertDateToTimeStamp(currentDate)

private fun Long.convertTimeStampToDate(): String = dateFormatter.format(this)
private fun convertDateToTimeStamp(date: String): Long = dateFormatter.parse(date).time