package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.common.Constants
import com.fatherofapps.androidbase.common.Constants.KEY_ADDRESS
import com.fatherofapps.androidbase.common.Constants.KEY_DOB
import com.fatherofapps.androidbase.common.Constants.KEY_GENDER
import com.fatherofapps.androidbase.common.Constants.KEY_NAME
import com.fatherofapps.androidbase.common.Constants.KEY_PHONE
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.response.ConfigResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PatientAPI {

   @GET("/patient/:id")
    suspend fun getPatientById(@Body id: String): Response<ConfigResponse<Any>>

    @Multipart
    @POST("/patient/update")
    suspend fun updatePatient(
        @Part(KEY_NAME) name: RequestBody,
        @Part(KEY_PHONE) phone: RequestBody,
        @Part(KEY_ADDRESS) address: RequestBody,
        @Part(KEY_DOB) dob: RequestBody,
        @Part(KEY_GENDER) gender: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ConfigResponse<Patient>>
}