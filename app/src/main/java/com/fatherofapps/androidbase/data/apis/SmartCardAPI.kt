package com.fatherofapps.androidbase.data.apis

import com.fatherofapps.androidbase.data.request.SmartCardRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SmartCardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SmartCardAPI {

    @POST("/smartCard/create")
    suspend fun createSmartCard(@Body smartCardRequest: SmartCardRequest): Response<ConfigResponse<Any>>
}