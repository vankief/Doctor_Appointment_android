package com.fatherofapps.androidbase.data.request

import java.io.File


data class updatePatient(
    val name: String,
    val phone: String,
    val gender: String,
    val dob: String, //"yyyy-MM-dd"
    val address: String,
    val img: File?,
)

data class registerNotification(
    val token: String
)

data class createReview(
    val rating: Int,
    val comment: String,
    val doctorId: String,
    val appointmentId: String

)
