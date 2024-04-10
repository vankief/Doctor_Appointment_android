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

