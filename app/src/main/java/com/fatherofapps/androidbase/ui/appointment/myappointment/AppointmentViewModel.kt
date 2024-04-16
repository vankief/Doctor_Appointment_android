package com.fatherofapps.androidbase.ui.appointment.myappointment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.response.AppointmentsResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _AppointmentsResponse = MutableLiveData<ConfigResponse<AppointmentsResponse>>()
    val AppointmentsResponse: MutableLiveData<ConfigResponse<AppointmentsResponse>>
        get() = _AppointmentsResponse

    fun getAppointments() {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = appointmentRepository.getAppointments()
            _AppointmentsResponse.postValue(response!!)
        }
        registerJobFinish()
    }
}