package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.models.TimeSlot
import com.fatherofapps.androidbase.data.request.DoctorFilter
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DoctorInfo
import com.fatherofapps.androidbase.data.response.ListTime
import com.fatherofapps.androidbase.data.response.TopDoctor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorAPI {

    @GET("/doctors/{DoctorPath}")
    suspend fun getDoctorById(@Path("DoctorPath") id: String,): Response<ConfigResponse<DoctorInfo>>

    @GET("/doctors")
    suspend fun getAllDoctors(@Body data: DoctorFilter): Response<ConfigResponse<Any>>

    @GET("/doctors/top")
    suspend fun getTopDoctors(): Response<ConfigResponse<List<TopDoctor>>>

    @GET("/doctorTimeSlot/appointment-time/{DoctorPath}")
    suspend fun getDoctorTimeSlot(@Path("DoctorPath") id: String, @Query("day") day: String): Response<ConfigResponse<List<ListTime>>>

}