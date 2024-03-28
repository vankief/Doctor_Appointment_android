package com.fatherofapps.androidbase.data.response

data class AppointmentResponse(
    val id: String,
    val doctorId: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val patientName: String,
    val patientPhone: String,
    val patientAge: String,
    val patientGender: String,
    val reason: String,
    val fee: Int,
    val service: String,
    val paymentType: String,
    val status: String,
)
data class PaymentDetailResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer:   String,
    val publishableKey: String
)
