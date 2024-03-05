package com.fatherofapps.androidbase.data.services

import android.util.Log
import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.AuthAPI
import com.fatherofapps.androidbase.data.request.AuthRequest
import com.fatherofapps.androidbase.data.request.SignUpRequest
import com.fatherofapps.androidbase.data.response.AuthResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SignUpResponse
import javax.inject.Inject

class AuthRemoteService @Inject constructor(private val authAPI: AuthAPI) : BaseRemoteService() {
    suspend fun login(authRequest: AuthRequest): NetworkResult<ConfigResponse<AuthResponse>> {
        return callApi { authAPI.login(authRequest) }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<ConfigResponse<SignUpResponse>> {
        return callApi { authAPI.signUp(signUpRequest) }
    }
}