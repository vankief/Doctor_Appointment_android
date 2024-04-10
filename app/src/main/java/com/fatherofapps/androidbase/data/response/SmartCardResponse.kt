package com.fatherofapps.androidbase.data.response

data class SmartCardResponse(
    val id: String,
    val cicNumber: String,
    val name: String,
    val gender: Boolean,
    val dob: String,
    val address: String,
    val phone: String,
    )