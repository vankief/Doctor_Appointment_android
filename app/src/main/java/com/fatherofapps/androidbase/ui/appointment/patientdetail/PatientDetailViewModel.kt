package com.fatherofapps.androidbase.ui.appointment.patientdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import com.fatherofapps.androidbase.data.repositories.AppointmentRepository
import com.fatherofapps.androidbase.data.request.AppointmentRequest
import com.fatherofapps.androidbase.data.response.ConfigResponse
import com.fatherofapps.androidbase.data.response.PaymentDetailResponse
import com.fatherofapps.androidbase.helper.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PatientDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val preferenceManager: PreferenceManager
): BaseViewModel(){

    private var _createAppointmentResponse = MutableLiveData<ConfigResponse<PaymentDetailResponse>>()
    val createAppointmentResponse: MutableLiveData<ConfigResponse<PaymentDetailResponse>>
        get() = _createAppointmentResponse

        fun createAppointment(appointmentRequest: AppointmentRequest) {
            showLoading(true)
            parentJob = viewModelScope.launch {
                val response = appointmentRepository.createAppointment(appointmentRequest)
                _createAppointmentResponse.postValue(response!!)
            }
            registerJobFinish()
        }




}