package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.services.DoctorRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DoctorRepository @Inject constructor(
    private val doctorRemoteService: DoctorRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
){
    suspend fun getTopDoctors() = withContext(dispatcher) {
        val result = doctorRemoteService.getTopDoctors()

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

    suspend fun getDoctorById(id: String) = withContext(dispatcher) {
        val result = doctorRemoteService.getDoctorById(id)

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

    suspend fun getDoctorTimeSlot(id: String, day: String) = withContext(dispatcher) {
        val result = doctorRemoteService.getDoctorTimeSlot(id, day)

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

    suspend fun getDoctorPrice(id: String) = withContext(dispatcher) {
        val result = doctorRemoteService.getDoctorPrice(id)

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