package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.BaseRemoteService.Companion.TAG
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.services.SpecialistRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpecialistRepository @Inject constructor(
    private val specialistRemoteService: SpecialistRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
){
    suspend fun getDoctorsBySpecialist() =
        withContext(dispatcher) {
            when (val result = specialistRemoteService.getDoctorsBySpecialist()) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "getDoctorsBySpecialist: ${result.data}")
                    return@withContext result.data
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "getDoctorsBySpecialist: ${result.exception.message}")
                    return@withContext null
                }
            }
        }

    suspend fun getSpecialists() =
        withContext(dispatcher) {
            when (val result = specialistRemoteService.getSpecialists()) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "getSpecialists: ${result.data}")
                    return@withContext result.data
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "getSpecialists: ${result.exception.message}")
                    return@withContext null
                }
            }
        }

}