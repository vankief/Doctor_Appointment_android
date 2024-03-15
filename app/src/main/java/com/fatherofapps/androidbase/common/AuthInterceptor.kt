package com.fatherofapps.androidbase.common

import android.util.Log
import com.fatherofapps.androidbase.common.Constants.ACCESS_TOKEN
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = preferenceManager.getString(
            ACCESS_TOKEN, ""
        )

        Log.d(TAG, "intercept: $accessToken")

        if(accessToken.isNullOrEmpty()) {
            throw Exception("Access token is null or empty")
        }


        // val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NTAwMGI5NTE5ZmY5NTJiOTQ0ZWJiMmQiLCJpYXQiOjE2OTY4NjI1ODMsImV4cCI6MTY5ODU5MDU4M30.39X8utixJ92BFUf6UfliG3V5Yu2NTqUC9gFYitQvj6I"
        Log.d(TAG, "intercept: $accessToken")
        val request = chain.request().newBuilder()

        request.addHeader("Authorization", "Bearer $accessToken")
        request.addHeader(
            "Content-Type", CONTENT_TYPE_JSON
        )
        return chain.proceed(request.build())
    }

    companion object {
        const val CONTENT_TYPE_JSON = "application/json"
        private const val TAG = "AuthInterceptor"

    }
}