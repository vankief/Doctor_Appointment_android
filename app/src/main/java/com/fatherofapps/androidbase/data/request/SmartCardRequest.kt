package com.fatherofapps.androidbase.data.request

data class SmartCardRequest(
    val cicNumber: String,
    val name: String,
    val gender: Boolean,
    val dob: String,
    val address: String,
    val phone: String,
)