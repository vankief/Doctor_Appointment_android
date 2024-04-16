package com.fatherofapps.androidbase.data.response

import com.fatherofapps.androidbase.utils.EStatus


data class AppointmentResponse(
    val paymentDetailOfflineResponse: PaymentDetailOfflineResponse?,
    val paymentDetailOnlineResponse: PaymentDetailOnlineResponse?
)
data class PaymentDetailOnlineResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer:   String,
    val publishableKey: String
)

data class PaymentDetailOfflineResponse(
    val appointmentId: String,
    val amountPad: Int,
    val paymentStatus: String
)
 data class AppointmentsResponse(
     val upcoming: List<DateAppointment>,
     val past: List<DateAppointment>
 )

data class Appointment(
    val appointmentId: String,
    val doctorName: String,
    val doctorImage: String,
    val service: String,
    val status: EStatus,
    val scheduleTime: String,
)
data class DateAppointment(
    val date: String,
    val appointments: List<Appointment>
)

data class AppointmentDetail(
    val appointmentId: String,
    val doctorName: String,
    val doctorImage: String,
    val totalPatients:  Int,
    val totalReviews: Int,
    val experience: String,
    val service: String,
    val status: EStatus,
    val scheduleTime: String,
    val scheduleDate: String,
    val patientName: String,
    val patientPhone: String,
    val patientAge: String,
    val fee : Int,
)