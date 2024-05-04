package com.fatherofapps.androidbase.data.response


data class PatientInfo(
    val id: String,
    val name: String,
    val email: String,
    val dob: String,
    val image: String
)

data class PatientDetail(
    val id: String,
    val name: String,
    val dob: String,
    val phone: String,
    val gender: Boolean,
)

data class DayNotification(
    val day: String,
    val notifications: List<Notification>
)

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val isRead: Boolean
)