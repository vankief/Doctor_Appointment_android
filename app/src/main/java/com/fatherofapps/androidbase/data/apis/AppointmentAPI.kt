package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.data.response.AppointmentResponse
import com.fatherofapps.androidbase.data.response.AppointmentsCompleted
import com.fatherofapps.androidbase.data.response.AppointmentsResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorAppointment
import com.fatherofapps.androidbase.data.response.PaymentDetailOnlineResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentAPI {
    @POST("/appointment/create")
    suspend fun createAppointment(@Body data: AppointmentRequest): Response<ConfigResponse<AppointmentResponse>>

    @GET("/appointment")
    suspend fun getAppointments(): Response<ConfigResponse<AppointmentsResponse>>

    @GET("/complete-appointment")
    suspend fun getCompleteAppointments(): Response<ConfigResponse<AppointmentsCompleted>>

    @GET("/appointment/patient/{id}")
    suspend fun getPatientAppointments(@Path("id") id: String): Response<ConfigResponse<AppointmentDetail>>


    @GET("/appointment/doctor/{id}")
    suspend fun getDoctorAppointments(@Path("id") id: String): Response<ConfigResponse<DoctorAppointment>>

    @POST("/appointment/payment/{id}")
    suspend fun paymentAppointment(@Path("id") id: String): Response<ConfigResponse<PaymentDetailOnlineResponse>>

}