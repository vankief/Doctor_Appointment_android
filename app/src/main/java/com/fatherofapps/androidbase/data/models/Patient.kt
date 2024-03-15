package com.fatherofapps.androidbase.data.models

data class Patient(
    val id: String,
    val img: String,
    val name: String,
    val email: String,
    val phone: String,
    val gender: Boolean,
    val dob: String, //"yyyy-MM-dd"
    val address: String,
)