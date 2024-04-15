package com.fatherofapps.androidbase.data.response


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

data class AppointmentDetail(
    val appointmentId: String,
    val doctorName: String,
    val doctorImage: String,
    val service: String,
    val status: String,
    val scheduleTime: String,
)
data class DateAppointment(
    val date: String,
    val appointments: List<AppointmentDetail>
)