package com.fatherofapps.androidbase.data.response

data class PaymentDetailOnlineResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer:   String,
    val publishableKey: String
)

data class PaymentDetailOfflineResponse(
    val paymentDetail: String,
)

