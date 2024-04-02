package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.DoctorAPI
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.data.response.TopDoctor
import com.fatherofapps.androidbase.data.response.TopDoctorBySpecialist
import com.fatherofapps.androidbase.data.response.doctorPrice
import javax.inject.Inject

class DoctorRemoteService @Inject constructor(private val doctorAPI: DoctorAPI) : BaseRemoteService() {
    suspend fun getTopDoctors(): NetworkResult<ConfigResponse<List<TopDoctor>>>
    {
        return callApi { doctorAPI.getTopDoctors() }
    }

    suspend fun getDoctorById(id: String): NetworkResult<ConfigResponse<DoctorInfo>>
    {
        return callApi { doctorAPI.getDoctorById(id) }
    }

    suspend fun getDoctorTimeSlot(id: String, day: String): NetworkResult<ConfigResponse<List<ListTime>>>
    {
        return callApi { doctorAPI.getDoctorTimeSlot(id, day) }
    }

    suspend fun getDoctorPrice(id: String): NetworkResult<ConfigResponse<doctorPrice>>
    {
        return callApi { doctorAPI.getDoctorPrice(id) }
    }

    suspend fun getDoctorBySpecialist(specialistId: String?): NetworkResult<ConfigResponse<List<TopDoctorBySpecialist>>>
    {
        return callApi { doctorAPI.getDoctorBySpecialist(specialistId) }
    }

}