package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.PaymentDetailOfflineResponse
import com.fatherofapps.androidbase.data.response.PaymentDetailOnlineResponse
import com.fatherofapps.androidbase.data.response._ConfigData
import com.fatherofapps.androidbase.data.response._ConfigResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AppointmentAPI {
    @POST("/appointment/create")
    suspend fun createAppointment(@Body data: AppointmentRequest): Response<ConfigResponse<Any>>

}