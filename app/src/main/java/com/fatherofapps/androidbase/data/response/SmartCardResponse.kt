package com.fatherofapps.androidbase.data.response

data class SmartCardResponse(
    val id: Int,
    val name: String,
    val gender: Boolean,
    val dob: String,
    val address: String,
    val phone: String,
    val balance: Int,
    val isBlocked: Boolean
    )