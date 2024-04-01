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
