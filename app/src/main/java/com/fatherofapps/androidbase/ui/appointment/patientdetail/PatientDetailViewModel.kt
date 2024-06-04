package com.fatherofapps.androidbase.ui.appointment.patientdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.repositories.PatientRepository
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.AppointmentResponse
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.PatientDetail
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private var _createAppointmentResponse = MutableLiveData<ConfigResponse<AppointmentResponse>>()
    val createAppointmentResponse: MutableLiveData<ConfigResponse<AppointmentResponse>>
        get() = _createAppointmentResponse

    private var _patientDetailResponse = MutableLiveData<ConfigResponse<PatientDetail>>()
    val patientDetailResponse: MutableLiveData<ConfigResponse<PatientDetail>>
        get() = _patientDetailResponse

    fun createAppointment(appointmentRequest: AppointmentRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = appointmentRepository.createAppointment(appointmentRequest)
            _createAppointmentResponse.postValue(response!!)
        }
        registerJobFinish()
    }

    fun getPatientDetail() {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val response = patientRepository.getPatientDetail()
            _patientDetailResponse.postValue(response!!)
        }
        registerJobFinish()
    }

}