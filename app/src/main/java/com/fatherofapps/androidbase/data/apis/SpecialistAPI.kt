package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.Specialist
import com.fatherofapps.androidbase.data.response.SpecialistDoctor
import retrofit2.Response
import retrofit2.http.GET

interface SpecialistAPI {

    @GET("/specialists/listdoctors")
    suspend fun getDoctorsBySpecialist(): Response<ConfigResponse<List<SpecialistDoctor>>>

    @GET("/specialists")
    suspend fun getSpecialists(): Response<ConfigResponse<List<Specialist>>>
}