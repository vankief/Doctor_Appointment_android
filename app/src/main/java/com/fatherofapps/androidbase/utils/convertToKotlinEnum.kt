package com.fatherofapps.androidbase.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class TimeSlot {
    T1,
    T2,
    T3,
    T4,
    T5,
    T6
}
fun convertToNormalTime(timeSlot: TimeSlot): String {
    return when (timeSlot) {
        TimeSlot.T1 -> "8:00 - 9:00"
        TimeSlot.T2 -> "9:00 - 10:00"
        TimeSlot.T3 -> "10:00 - 11:00"
        TimeSlot.T4 -> "13:00 - 14:00"
        TimeSlot.T5 -> "14:00 - 15:00"
        TimeSlot.T6 -> "15:00 - 16:00"
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
    // Phân tích ngày từ chuỗi
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dayString, formatter)
    // Định dạng ngày theo kiểu "Thứ x, ngày dd/MM/yyyy"
    val dayOfWeek = date.dayOfWeek
    val dayOfWeekString = convertToVietnameseDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toString()))
    // Định dạng lại chuỗi ngày theo định dạng yêu cầu
    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    return "$dayOfWeekString, $formattedDate"
}