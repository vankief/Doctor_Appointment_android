package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.DoctorAPI
import com.fatherofapps.androidbase.data.apis.PatientAPI
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.response.ConfigResponse
import retrofit2.http.Body
import javax.inject.Inject

class PatientRemoteService @Inject constructor(private val patientAPI: PatientAPI): BaseRemoteService(){
    suspend fun updatePatient(@Body data: updatePatient): NetworkResult<ConfigResponse<Patient>> {
        return callApi { patientAPI.updatePatient(data) }
    }
}