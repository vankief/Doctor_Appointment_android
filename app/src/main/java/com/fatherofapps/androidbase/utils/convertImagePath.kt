package com.fatherofapps.androidbase.utils

import com.fatherofapps.androidbase.BuildConfig

fun convertImagePath(path: String): String {
    return BuildConfig.BASE_URL + path
}