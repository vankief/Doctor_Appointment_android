package com.fatherofapps.androidbase.data.response

import com.fatherofapps.androidbase.data.models.Specialist

data class TopDoctor(
    val id: String,
    val name: String,
    val img: String,
    val specialist: String,
)

data class Doctor(
    val id: String,
    val name: String,
    val imageUrl: String,
    val specialist: Specialist,
    val rating: Float,
    val reviewCount: Int,
    val experience: Int,
    val about: String,
    val location: String,
    val phone: String,
    val email: String,
    val address: String
)
