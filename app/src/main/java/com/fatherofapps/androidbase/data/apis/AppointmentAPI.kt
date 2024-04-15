package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.AppointmentResponse
import com.fatherofapps.androidbase.data.response.AppointmentsResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppointmentAPI {
    @POST("/appointment/create")
    suspend fun createAppointment(@Body data: AppointmentRequest): Response<ConfigResponse<AppointmentResponse>>

    @GET("/appointment")
    suspend fun getAppointments(): Response<ConfigResponse<AppointmentsResponse>>


}