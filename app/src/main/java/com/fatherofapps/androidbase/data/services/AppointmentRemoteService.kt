package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.AppointmentAPI
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import javax.inject.Inject

class AppointmentRemoteService @Inject constructor( private val appointmentAPI: AppointmentAPI) : BaseRemoteService(){
    suspend fun createAppointment(data: AppointmentRequest): NetworkResult<ConfigResponse<Any>> {
        return callApi { appointmentAPI.createAppointment(data) }
    }
}