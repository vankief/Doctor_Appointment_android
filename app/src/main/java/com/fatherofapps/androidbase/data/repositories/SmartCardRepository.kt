package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.request.SmartCardRequest
import com.fatherofapps.androidbase.data.services.SmartCardRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmartCardRepository @Inject constructor(
    private val smartCardRemoteService: SmartCardRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun createSmartCard(data: SmartCardRequest) = withContext(dispatcher) {
        val result = smartCardRemoteService.createSmartCard(data)

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