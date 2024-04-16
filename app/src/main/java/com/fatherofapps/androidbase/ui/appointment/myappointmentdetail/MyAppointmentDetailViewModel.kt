package com.fatherofapps.androidbase.ui.appointment.myappointmentdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.response.AppointmentDetail
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MyAppointmentDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel() {

    private var _AppointmentsResponse = MutableLiveData<ConfigResponse<AppointmentDetail>>()
    val AppointmentsResponse: MutableLiveData<ConfigResponse<AppointmentDetail>>
        get() = _AppointmentsResponse

    fun getPatientAppointments(id: String) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = appointmentRepository.getPatientAppointments(id)
            _AppointmentsResponse.postValue(response!!)
        }
        registerJobFinish()
    }

}