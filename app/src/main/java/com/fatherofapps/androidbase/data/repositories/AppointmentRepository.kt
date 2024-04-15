package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.services.AppointmentRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val appointmentRemoteService: AppointmentRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
)  {
    suspend fun createAppointment(req: AppointmentRequest) = withContext(dispatcher) {
        val result = appointmentRemoteService.createAppointment(req)

        when (result) {
            is NetworkResult.Success -> {
                return@withContext result.data
            }
            is NetworkResult.Error -> {
                Log.e("Error", "getPosts: ${result.exception ?: "Error"}")
                return@withContext null
            }
        }

    }

    suspend fun getAppointments() = withContext(dispatcher) {
        val result = appointmentRemoteService.getAppointments()

        when (result) {
            is NetworkResult.Success -> {
                return@withContext result.data
            }
            is NetworkResult.Error -> {
                Log.e("Error", "getPosts: ${result.exception ?: "Error"}")
                return@withContext null
            }
        }

    }
}