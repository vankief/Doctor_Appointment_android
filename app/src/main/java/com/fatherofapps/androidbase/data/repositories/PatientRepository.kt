package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.BaseRemoteService.Companion.TAG
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.models.Patient
import com.fatherofapps.androidbase.data.request.updatePatient
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.services.PatientRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val patientRemoteService: PatientRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun updatePatient(updatePatient: updatePatient) =
        withContext(dispatcher) {
            when (val result =
                patientRemoteService.updatePatient(updatePatient)) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "updatePatient: ${result.data}")
                    return@withContext result.data
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "updatePatient: ${result.exception.message}")
                    return@withContext null
                }
            }
        }
}