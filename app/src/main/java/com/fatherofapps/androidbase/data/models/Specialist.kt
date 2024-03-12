package com.fatherofapps.androidbase.data.models

data class Specialist(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val doctors: List<Doctor>
) {
}