package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.data.apis.DoctorAPI
import com.fatherofapps.androidbase.data.request.DoctorFilter
import javax.inject.Inject

class DoctorRemoteService @Inject constructor(private val doctorAPI: DoctorAPI) : BaseRemoteService() {

    suspend fun getDoctorById(id: String) = callApi { doctorAPI.getDoctorById(id) }

    suspend fun getAllDoctors(data: DoctorFilter) = callApi { doctorAPI.getAllDoctors(data) }

    suspend fun getTopDoctors() = callApi { doctorAPI.getTopDoctors() }

}