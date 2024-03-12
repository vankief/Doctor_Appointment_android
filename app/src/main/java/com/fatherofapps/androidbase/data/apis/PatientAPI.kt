package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.response.ConfigResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatientAPI {

   @GET("/patient/:id")
    suspend fun getPatientById(@Body id: String): Response<ConfigResponse<Any>>

    @PATCH("/patient/:id")
    suspend fun updatePatient(): Response<ConfigResponse<Any>>

}