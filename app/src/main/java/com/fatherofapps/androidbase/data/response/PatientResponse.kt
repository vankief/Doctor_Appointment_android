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