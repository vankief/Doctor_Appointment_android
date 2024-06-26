package com.fatherofapps.androidbase.data.response

import com.fatherofapps.androidbase.data.models.Specialist
import com.fatherofapps.androidbase.utils.TimeSlot
import java.util.Date

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
    val currentPatient: Int
)

data class doctorPrice(
    val id: String,
    val onlinePrice: Int,
    val offlinePrice: Int,
)

data class DoctorAppointment(
    val doctorId: String,
    val doctorName: String,
    val doctorImage: String,
)

data class ReViewResponse(
    val id: String,
    val patientName: String,
    val patientImage: String,
    val rating: Int,
    val comment: String,
    val createdDate: String,
)