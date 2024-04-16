package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.common.Constants
import com.fatherofapps.androidbase.data.apis.PatientAPI
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.request.registerNotification
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.PatientDetail
import com.fatherofapps.androidbase.data.response.PatientInfo
import com.fatherofapps.androidbase.utils.createPartFromString
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PatientRemoteService @Inject constructor(private val patientAPI: PatientAPI): BaseRemoteService(){
    suspend fun updatePatient(
        updatePatient: updatePatient
    ): NetworkResult<ConfigResponse<Patient>> {
        val nameRequestBody: RequestBody = RequestBody.create("Multipart/form-data".toMediaTypeOrNull(), updatePatient.name)
        val phoneRequestBody: RequestBody = RequestBody.create("Multipart/form-data".toMediaTypeOrNull(), updatePatient.phone)
        val addressRequestBody: RequestBody = RequestBody.create("Multipart/form-data".toMediaTypeOrNull(), updatePatient.address)
        val dobRequestBody: RequestBody = RequestBody.create("Multipart/form-data".toMediaTypeOrNull(), updatePatient.dob)
        val genderRequestBody: RequestBody = RequestBody.create("Multipart/form-data".toMediaTypeOrNull(), updatePatient.gender)
        val imageRequestBody: RequestBody = updatePatient.img?.let {
            RequestBody.create("Multipart/form-data".toMediaTypeOrNull(),
                it
            )
        }!!
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("image",updatePatient.img.name, imageRequestBody)
        return callApi { patientAPI.updatePatient(nameRequestBody, phoneRequestBody, addressRequestBody, dobRequestBody, genderRequestBody, imagePart) }
    }

    suspend fun getPatientById(): NetworkResult<ConfigResponse<PatientInfo>> {
        return callApi { patientAPI.getPatientById() }
    }

    suspend fun registerNotification(token: registerNotification): NetworkResult<ConfigResponse<Any>> {
        return callApi { patientAPI.registerNotification(token) }
    }

    suspend fun getPatientDetail(): NetworkResult<ConfigResponse<PatientDetail>> {
        return callApi { patientAPI.getPatientDetail() }
    }
}