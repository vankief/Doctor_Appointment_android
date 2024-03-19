package com.fatherofapps.androidbase.data.response

import com.fatherofapps.androidbase.data.models.Specialist

data class TopDoctor(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
)

data class DoctorInfo(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
    val averageRating: Double,
    val totalRatings: Int,
    val totalPatients: Int,
    val totalReviews: Int,
    val experience: String,
    val description: String
)
