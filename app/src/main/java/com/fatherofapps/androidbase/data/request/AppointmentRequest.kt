package com.fatherofapps.androidbase.data.request

data class AppointmentRequest (
    val doctorId: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val patientName: String,
    val patientPhone: String,
    val patientAge: String,
    val patientGender: Boolean,
    val reason: String,
    val fee: Int,
    val service: String,
)