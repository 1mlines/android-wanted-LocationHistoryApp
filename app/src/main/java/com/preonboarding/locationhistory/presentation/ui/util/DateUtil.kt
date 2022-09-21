import java.text.SimpleDateFormat
import java.util.*

val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
val dateTimeFormatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)

fun Long.convertTimeStampToDate(): String =
    dateFormatter.format(this)

fun Long.convertTimeStampToDateTime(): String =
    dateTimeFormatter.format(this)

fun getCurrentTime(): Long = System.currentTimeMillis()