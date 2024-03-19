package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.DoctorAPI
import com.fatherofapps.androidbase.data.request.DoctorFilter
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.data.response.TopDoctor
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

}