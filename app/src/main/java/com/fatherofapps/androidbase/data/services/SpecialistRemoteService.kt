package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.DoctorAPI
import com.fatherofapps.androidbase.data.apis.SpecialistAPI
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import javax.inject.Inject

class SpecialistRemoteService @Inject constructor(private val specialistAPI: SpecialistAPI): BaseRemoteService() {
    suspend fun getDoctorsBySpecialist(): NetworkResult<ConfigResponse<List<SpecialistDoctor>>> {
        return callApi { specialistAPI.getDoctorsBySpecialist() }
    }


}