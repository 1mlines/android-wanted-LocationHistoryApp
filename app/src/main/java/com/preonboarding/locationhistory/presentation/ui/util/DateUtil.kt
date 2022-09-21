package com.preonboarding.locationhistory.presentation.ui.util

import java.text.SimpleDateFormat
import java.util.Locale

val currentDate: String = System.currentTimeMillis().convertTimeStampToDate()
val currentTimeStamp: Long = convertDateToTimeStamp(currentDate)

private val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
private fun Long.convertTimeStampToDate(): String = dateFormatter.format(this)
private fun convertDateToTimeStamp(date: String): Long = dateFormatter.parse(date).time