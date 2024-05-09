package com.fatherofapps.androidbase.data.services

import com.fatherofapps.androidbase.base.network.BaseRemoteService
import com.fatherofapps.androidbase.base.network.NetworkResult
import com.fatherofapps.androidbase.data.apis.SmartCardAPI
import com.fatherofapps.androidbase.data.request.SmartCardRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.SmartCardResponse
import javax.inject.Inject

class SmartCardRemoteService @Inject constructor(private val smartCardAPI: SmartCardAPI): BaseRemoteService(){

    suspend fun createSmartCard(data: SmartCardRequest) : NetworkResult<ConfigResponse<Any>> {
        return callApi { smartCardAPI.createSmartCard(data) }
    }

    suspend fun getSmartCardInfo() : NetworkResult<ConfigResponse<SmartCardResponse>> {
        return callApi { smartCardAPI.getSmartCardInfo() }
    }

    suspend fun blockSmartCard() : NetworkResult<ConfigResponse<Any>> {
        return callApi { smartCardAPI.blockSmartCard() }
    }

    suspend fun unblockSmartCard() : NetworkResult<ConfigResponse<Any>> {
        return callApi { smartCardAPI.unblockSmartCard() }
    }
}