package com.fatherofapps.androidbase.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class TimeSlot {
    T1,
    T2,
    T3,
    T4,
    T5,
    T6,
    T7,
    T8,
}
fun convertToNormalTime(timeSlot: TimeSlot): String {
    return when (timeSlot) {
        TimeSlot.T1 -> "9:00 - 10:00"
        TimeSlot.T2 -> "10:00 - 11:00"
        TimeSlot.T3 -> "11:00 - 12:00"
        TimeSlot.T4 -> "13:00 - 14:00"
        TimeSlot.T5 -> "14:00 - 15:00"
        TimeSlot.T6 -> "15:00 - 16:00"
        TimeSlot.T7 -> "16:00 - 17:00"
        TimeSlot.T8 -> "17:00 - 18:00"
    }
}
enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

fun convertToVietnameseDayOfWeek(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Thứ 2"
        DayOfWeek.TUESDAY -> "Thứ 3"
        DayOfWeek.WEDNESDAY -> "Thứ 4"
        DayOfWeek.THURSDAY -> "Thứ 5"
        DayOfWeek.FRIDAY -> "Thứ 6"
        DayOfWeek.SATURDAY -> "Thứ 7"
        DayOfWeek.SUNDAY -> "Chủ Nhật"
    }
}
fun convertToVietNamDate(dayString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dayString, formatter)
    val dayOfWeek = date.dayOfWeek
    val dayOfWeekString = convertToVietnameseDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toString()))
    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    return "$dayOfWeekString, $formattedDate"
}

fun convertToNormalDate(dayString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dayString, formatter)
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun convertDateFormatAndDayOfWeek(dateString: String): Pair<String, String> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    val date = inputFormat.parse(dateString)
    val formattedDate = outputFormat.format(date)
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    return Pair(formattedDate, dayOfWeek)
}