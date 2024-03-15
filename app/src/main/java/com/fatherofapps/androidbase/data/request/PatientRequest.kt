package com.fatherofapps.androidbase.data.request

class PatientRequest {
}

data class updatePatient(
    val img: String,
    val name: String,
    val phone: String,
    val gender: Boolean,
    val dob: String, //"yyyy-MM-dd"
    val address: String,
)