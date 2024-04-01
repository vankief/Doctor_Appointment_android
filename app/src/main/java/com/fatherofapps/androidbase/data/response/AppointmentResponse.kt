package com.fatherofapps.androidbase.data.response

data class PaymentDetailResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer:   String,
    val publishableKey: String
)
