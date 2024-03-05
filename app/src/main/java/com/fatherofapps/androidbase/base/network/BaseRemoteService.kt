package com.fatherofapps.androidbase.base.network

import android.util.Log
import retrofit2.Response

open class BaseRemoteService : BaseService() {
    companion object {
        const val TAG = "BaseRemoteService"
    }

    protected suspend fun <T : Any> callApi(call: suspend () -> Response<T>): NetworkResult<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            Log.e(TAG, "callApi: ${t.message}")
            return NetworkResult.Error(parseNetworkErrorException(t))
        }

        return if (response.isSuccessful) {
            if (response.body() == null) {
                NetworkResult.Error(
                    BaseNetworkException(
                        responseMessage = "Response without body",
                        responseCode = 200
                    )
                )
            } else {
                NetworkResult.Success(response.body()!!)
            }
        } else {
            val errorBody = response.errorBody()?.string() ?: ""
            Log.e(TAG, "callApi: $response")
            NetworkResult.Error(parseError(response.message(), response.code(), errorBody))
        }
    }
}