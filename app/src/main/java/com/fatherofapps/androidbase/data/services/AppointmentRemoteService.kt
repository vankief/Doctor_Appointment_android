package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.AppointmentAPI
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.data.response.AppointmentResponse
import com.fatherofapps.androidbase.data.response.AppointmentsResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorAppointment
import javax.inject.Inject

class AppointmentRemoteService @Inject constructor( private val appointmentAPI: AppointmentAPI) : BaseRemoteService(){
    suspend fun createAppointment(data: AppointmentRequest): NetworkResult<ConfigResponse<AppointmentResponse>> {
        return callApi { appointmentAPI.createAppointment(data) }
    }

    suspend fun getAppointments(): NetworkResult<ConfigResponse<AppointmentsResponse>> {
        return callApi { appointmentAPI.getAppointments() }
    }

    suspend fun getPatientAppointments(id: String): NetworkResult<ConfigResponse<AppointmentDetail>> {
        return callApi { appointmentAPI.getPatientAppointments(id) }
    }

    suspend fun getDoctorAppointments(id: String): NetworkResult<ConfigResponse<DoctorAppointment>> {
        return callApi { appointmentAPI.getDoctorAppointments(id) }
    }
}