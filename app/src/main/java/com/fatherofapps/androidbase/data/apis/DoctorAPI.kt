package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.models.Doctor
import com.fatherofapps.androidbase.data.request.DoctorFilter
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import com.fatherofapps.androidbase.data.response.SpecialistResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface DoctorAPI {

    @GET("/doctors/:id")
    suspend fun getDoctorById(@Body id: String): Response<ConfigResponse<Doctor>>

    @GET("/doctors")
    suspend fun getAllDoctors(@Body data: DoctorFilter): Response<ConfigResponse<Any>>


}