package com.fatherofapps.androidbase.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppointmentInfo(
    val doctorId: String,
    val day: String,
    val time: String,
    val price: Int,
    val service: String
): Parcelable