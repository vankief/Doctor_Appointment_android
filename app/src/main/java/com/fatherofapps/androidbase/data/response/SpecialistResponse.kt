package com.fatherofapps.androidbase.data.response

class SpecialistResponse {
}

data class SpecialistDoctor(
    val id: String,
    val name: String,
    val numberOfDoctors: Int,
)

data class Specialist(
    val id: String,
    val name: String,
)

data class DoctorofSpecialist(
    val name: String,
    val email: String,
    val address: String,
    val img: String,
    val phone: String,
    val gender: Boolean,
    val dob: String,
    val price: Int,
    val services: String,
    val degree: String,
    val college: String,
    val experience: String,
    val designation: String,
    val awards: String,
)