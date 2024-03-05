package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.request.AuthRequest
import com.fatherofapps.androidbase.data.request.SignUpRequest
import com.fatherofapps.androidbase.data.response.AuthResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<ConfigResponse<AuthResponse>>
    @POST("/patient/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<ConfigResponse<SignUpResponse>>
}