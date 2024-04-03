package com.fatherofapps.androidbase.data.response

import com.fatherofapps.androidbase.data.models.Specialist
import com.fatherofapps.androidbase.utils.TimeSlot

data class TopDoctor(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
)

data class TopDoctorBySpecialist(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
    val averageRating: Float,
    val totalReviews: Int,
    val isFavorite: Boolean
)

data class DoctorInfo(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
    val averageRating: Float,
    val totalRatings: Int,
    val totalPatients: Int,
    val totalReviews: Int,
    val experience: String,
    val description: String
)

data class ListTime(
    val id: String,
    val timeSlot: TimeSlot,
    val service: String,
    val maximumPatient: Int,
)

data class doctorPrice(
    val id: String,
    val price: Int,
)