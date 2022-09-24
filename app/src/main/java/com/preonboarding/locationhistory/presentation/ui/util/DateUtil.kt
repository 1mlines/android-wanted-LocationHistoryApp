package com.preonboarding.locationhistory.presentation.ui.util

import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
fun Long.convertTimeStampToDate(): String = dateFormatter.format(this)

private val dateTimeFormatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
fun Long.convertTimeStampToDateWithTime(): String = dateTimeFormatter.format(this)
