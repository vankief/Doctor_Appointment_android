package com.fatherofapps.androidbase.data.repositories

import android.util.Log
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.request.AuthRequest
import com.fatherofapps.androidbase.data.request.SignUpRequest
import com.fatherofapps.androidbase.data.services.AuthRemoteService
import com.fatherofapps.androidbase.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteService: AuthRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun doLogin(req: AuthRequest) = withContext(dispatcher) {
        val result = authRemoteService.login(req)

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
    suspend fun doSignUp(req: SignUpRequest) = withContext(dispatcher) {
        val result = authRemoteService.signUp(req)

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