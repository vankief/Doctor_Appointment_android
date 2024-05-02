package com.fatherofapps.androidbase.utils

import com.fatherofapps.androidbase.BuildConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun convertImagePath(path: String): String {
    return if (path.startsWith("http://") || path.startsWith("https://")) {
        path
    } else {
        BuildConfig.BASE_URL + path
    }
}

fun createPartFromString(stringData: String): RequestBody {
    return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
}