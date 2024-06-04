package com.fatherofapps.androidbase.data.models

data class TimeSlotInfo(
    val normalTime: String,
    val service: String,
    val maximumPatient: Int,
    val currentPatient: Int
)
