package com.fatherofapps.androidbase.data.response

class AuthResponse (
    val accessToken: String,
    val refreshToken: String,
)

data class User(
    val email: String,
    val userId: String,
    val role: String
)