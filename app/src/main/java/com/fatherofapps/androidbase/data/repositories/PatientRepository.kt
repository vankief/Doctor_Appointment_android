package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.services.PatientRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val patientRemoteService: PatientRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun updatePatient(data: updatePatient) = withContext(dispatcher) {
        val result = patientRemoteService.updatePatient(data)

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