package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.common.Constants.KEY_ADDRESS
import com.fatherofapps.androidbase.common.Constants.KEY_DOB
import com.fatherofapps.androidbase.common.Constants.KEY_GENDER
import com.fatherofapps.androidbase.common.Constants.KEY_NAME
import com.fatherofapps.androidbase.common.Constants.KEY_PHONE
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.request.createReview
import com.fatherofapps.androidbase.data.request.registerNotification
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.DayNotification
import com.fatherofapps.androidbase.data.response.PatientDetail
import com.fatherofapps.androidbase.data.response.PatientInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PatientAPI {

   @GET("/patient/profile")
    suspend fun getPatientById(): Response<ConfigResponse<PatientInfo>>

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

    @POST("/register")
    suspend fun registerNotification(@Body token: registerNotification): Response<ConfigResponse<Any>>

    @GET("/patient/detail")
    suspend fun getPatientDetail(): Response<ConfigResponse<PatientDetail>>

    @POST("/reviews")
    suspend fun createReview(@Body review: createReview): Response<ConfigResponse<Any>>

    @GET("/notification/get-all")
    suspend fun getNotifications(): Response<ConfigResponse<List<DayNotification>>>

    @GET("/notification/unread")
    suspend fun getUnreadNotifications(): Response<ConfigResponse<Int>>

    @POST("/notification/read-all")
    suspend fun markAllNotificationAsRead(): Response<ConfigResponse<List<DayNotification>>>


}